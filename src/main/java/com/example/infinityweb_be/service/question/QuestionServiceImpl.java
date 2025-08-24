// QuestionServiceImpl.java
package com.example.infinityweb_be.service.question;

import com.example.infinityweb_be.domain.*;
import com.example.infinityweb_be.domain.course.Course;
import com.example.infinityweb_be.domain.dto.question.admin.QuestionCreateDto;
import com.example.infinityweb_be.domain.dto.question.admin.QuestionResponseDto;
import com.example.infinityweb_be.domain.dto.question.admin.OptionCreateDto; // Thêm import
import com.example.infinityweb_be.domain.dto.question.admin.AnswerCreateDto; // Thêm import
import com.example.infinityweb_be.domain.mapper.QuestionMapper;
import com.example.infinityweb_be.repository.*;
import com.example.infinityweb_be.repository.question.QuestionAnswerRepository;
import com.example.infinityweb_be.repository.question.QuestionOptionRepository;
import com.example.infinityweb_be.repository.question.QuestionRepository;
import com.example.infinityweb_be.repository.question.QuestionTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepo;
    private final QuestionOptionRepository optionRepo;
    private final QuestionAnswerRepository answerRepo;
    private final QuestionTypeRepository typeRepo;
    private final LessonRepository lessonRepo;
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;

    @Autowired
    private QuestionMapper mapper;

    @PersistenceContext
    private EntityManager em;

    public List<QuestionResponseDto> getAll() {
        List<Question> questions = questionRepo.findAll();
        return questions.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }



    private void setSessionContext(int adminId) {
        em.createNativeQuery("EXEC sp_set_session_context 'user_id', :uid")
                .setParameter("uid", adminId)
                .executeUpdate();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public QuestionResponseDto getById(Integer id) {
        return questionRepo.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Question not found: " + id));
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<QuestionResponseDto> getByLessonId(Integer lessonId) {
        return questionRepo.findByLesson_Id(lessonId).stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

// QuestionServiceImpl.java (ĐÃ SỬA LỖI MAPPING)

    // QuestionServiceImpl.java - Sửa method create
    @Override
    public QuestionResponseDto create(QuestionCreateDto dto, Integer adminId) {
        setSessionContext(adminId);

        Question question = new Question();
        question.setQuestionText(dto.getQuestionText());
        question.setDifficulty(dto.getDifficulty());
        question.setPoints(dto.getPoints());
        question.setCreatedBy(userRepo.getReferenceById(adminId));
        question.setCreatedAt(LocalDateTime.now());

        // Set quan hệ
        Lesson lesson = lessonRepo.findById(dto.getLessonId())
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + dto.getLessonId()));
        question.setLesson(lesson);

        QuestionType questionType = typeRepo.findById(dto.getQuestionTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Question type not found: " + dto.getQuestionTypeId()));
        question.setQuestionType(questionType);

        // Xử lý options - SỬA QUAN TRỌNG
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            List<QuestionOption> options = dto.getOptions().stream()
                    .map(optDto -> {
                        QuestionOption option = new QuestionOption();
                        option.setQuestion(question); // Set quan hệ
                        option.setOptionText(optDto.getOptionText());
                        option.setCorrect(optDto.isCorrect());
                        option.setPosition(optDto.getPosition());
                        option.setImageUrl(optDto.getImageUrl());
                        return option;
                    })
                    .collect(Collectors.toList());
            question.setOptions(options); // Set danh sách options vào question
        }

        // Xử lý answers
        if (dto.getAnswers() != null && !dto.getAnswers().isEmpty()) {
            List<QuestionAnswer> answers = dto.getAnswers().stream()
                    .map(ansDto -> {
                        QuestionAnswer answer = new QuestionAnswer();
                        answer.setQuestion(question); // Set quan hệ
                        answer.setAnswerText(ansDto.getAnswerText());
                        answer.setCaseSensitive(ansDto.isCaseSensitive());
                        answer.setPosition(ansDto.getPosition());
                        return answer;
                    })
                    .collect(Collectors.toList());
            question.setAnswers(answers); // Set danh sách answers vào question
        }

        // Chỉ cần save question 1 lần - cascade sẽ tự động save options/answers
        Question savedQuestion = questionRepo.save(question);

        return getById(savedQuestion.getId());
    }

    @Override
    public QuestionResponseDto update(Integer id, QuestionCreateDto dto, Integer adminId) {
        setSessionContext(adminId);

        Question existingQuestion = questionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found: " + id));

        // Chỉ update các trường có giá trị trong DTO
        if (dto.getQuestionText() != null) {
            existingQuestion.setQuestionText(dto.getQuestionText());
        }
        if (dto.getDifficulty() != null) {
            existingQuestion.setDifficulty(dto.getDifficulty());
        }
        if (dto.getPoints() != null) {
            existingQuestion.setPoints(dto.getPoints());
        }

        existingQuestion.setUpdatedAt(LocalDateTime.now());
        existingQuestion.setUpdatedBy(userRepo.getReferenceById(adminId));

        // Chỉ update quan hệ nếu có giá trị
        if (dto.getCourseId() != null) {
            Course course = courseRepo.findById(dto.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found: " + dto.getCourseId()));
            existingQuestion.setCourse(course);
        }

        if (dto.getLessonId() != null) { // ✅ CHỈ update lesson nếu có giá trị
            Lesson lesson = lessonRepo.findById(dto.getLessonId())
                    .orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + dto.getLessonId()));
            existingQuestion.setLesson(lesson);
        }

        if (dto.getQuestionTypeId() != null) {
            QuestionType questionType = typeRepo.findById(dto.getQuestionTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("Question type not found: " + dto.getQuestionTypeId()));
            existingQuestion.setQuestionType(questionType);
        }

        // 4. Cập nhật media (Giữ nguyên)
        if (dto.getMedia() != null) {
            existingQuestion.setMediaUrl(dto.getMedia().getMediaUrl());
            existingQuestion.setAudioUrl(dto.getMedia().getAudioUrl());
            existingQuestion.setVideoUrl(dto.getMedia().getVideoUrl());
        } else {
            existingQuestion.setMediaUrl(null);
            existingQuestion.setAudioUrl(null);
            existingQuestion.setVideoUrl(null);
        }

        // 5. Lưu lại Question đã cập nhật
        Question updatedQuestion = questionRepo.save(existingQuestion);

        // 6. XOÁ TOÀN BỘ OPTIONS & ANSWERS CŨ
        optionRepo.deleteByQuestion_Id(id);
        answerRepo.deleteByQuestion_Id(id);

        // 7. TẠO MỚI OPTIONS & ANSWERS TỪ DTO - SỬA TẠI ĐÂY: Không dùng toEntity()
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            for (OptionCreateDto optDto : dto.getOptions()) {
                QuestionOption option = new QuestionOption();
                option.setQuestion(updatedQuestion);
                option.setOptionText(optDto.getOptionText());
                option.setCorrect(optDto.isCorrect());
                option.setPosition(optDto.getPosition());
                option.setImageUrl(optDto.getImageUrl());
                optionRepo.save(option);
            }
        }

        if (dto.getAnswers() != null && !dto.getAnswers().isEmpty()) {
            for (AnswerCreateDto ansDto : dto.getAnswers()) {
                QuestionAnswer answer = new QuestionAnswer();
                answer.setQuestion(updatedQuestion);
                answer.setAnswerText(ansDto.getAnswerText());
                answer.setCaseSensitive(ansDto.isCaseSensitive());
                answer.setPosition(ansDto.getPosition());
                answerRepo.save(answer);
            }
        }

        // 8. Trả về response
        return getById(id);
    }




    @Override
    public void delete(Integer id) {
        questionRepo.deleteById(id);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public void validateQuestionBeforeUse(Integer questionId) {
        long count = optionRepo.findByQuestionId(questionId).size();
        if (count < 2) throw new IllegalStateException("At least two options required");
    }
}
