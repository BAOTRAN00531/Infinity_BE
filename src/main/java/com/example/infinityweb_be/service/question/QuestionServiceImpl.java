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

        // Optional course
        if (dto.getCourseId() != null) {
            Course course = courseRepo.findById(dto.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found: " + dto.getCourseId()));
            question.setCourse(course);
        }

        // Media
        if (dto.getMedia() != null) {
            question.setMediaUrl(dto.getMedia().getMediaUrl());
            question.setAudioUrl(dto.getMedia().getAudioUrl());
            question.setVideoUrl(dto.getMedia().getVideoUrl());
        }

        // Validate DTO against type constraints before persisting
        validateDtoAgainstType(dto, questionType);

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
            // Re-validate on type change
            validateDtoAgainstType(dto, questionType);
        } else if ((dto.getOptions() != null && !dto.getOptions().isEmpty()) || (dto.getAnswers() != null && !dto.getAnswers().isEmpty())) {
            // Validate against current type when payload modifies options/answers but not type
            validateDtoAgainstType(dto, existingQuestion.getQuestionType());
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
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found: " + questionId));

        QuestionType type = question.getQuestionType();
        List<QuestionOption> options = optionRepo.findByQuestionId(questionId);
        List<QuestionAnswer> answers = answerRepo.findByQuestionId(questionId);

        validateCounts(type, options, answers);
    }

    // ===================== Helpers =====================
    private void validateDtoAgainstType(QuestionCreateDto dto, QuestionType questionType) {
        int optCount = dto.getOptions() == null ? 0 : dto.getOptions().size();
        int correctCount = dto.getOptions() == null ? 0 : (int) dto.getOptions().stream().filter(OptionCreateDto::isCorrect).count();
        int ansCount = dto.getAnswers() == null ? 0 : dto.getAnswers().size();

        // Generic constraints from Question_Types
        if (questionType.getMinOptions() != null && questionType.getMinOptions() > 0 && optCount < questionType.getMinOptions()) {
            throw new IllegalArgumentException("Need at least %d options".formatted(questionType.getMinOptions()));
        }
        if (questionType.getMinCorrect() != null && questionType.getMinCorrect() > 0 && correctCount < questionType.getMinCorrect()) {
            throw new IllegalArgumentException("Need at least %d correct options".formatted(questionType.getMinCorrect()));
        }
        if (questionType.getMaxCorrect() != null && questionType.getMaxCorrect() > 0 && correctCount > questionType.getMaxCorrect()) {
            throw new IllegalArgumentException("At most %d correct options".formatted(questionType.getMaxCorrect()));
        }

        String code = questionType.getCode();
        if (code == null) return;
        switch (code) {
            case "text_input" -> {
                if (optCount > 0) throw new IllegalArgumentException("text_input should not have options");
                if (ansCount < 1) throw new IllegalArgumentException("text_input requires at least 1 answer");
            }
            case "reorder_words" -> {
                int min = questionType.getMinOptions() != null ? questionType.getMinOptions() : 3;
                if (optCount < Math.max(3, min)) throw new IllegalArgumentException("reorder_words requires at least 3 options");
                if (ansCount != optCount) throw new IllegalArgumentException("reorder_words must provide answers with positions equal to number of options");
                ensurePositionsAre1ToN(dto.getAnswers().stream().map(AnswerCreateDto::getPosition).collect(Collectors.toList()), optCount,
                        "answers positions for reorder_words must be 1..N without duplicates");
            }
            case "single_choice_image" -> {
                if (optCount < 2) throw new IllegalArgumentException("single_choice_image requires at least 2 options");
                if (correctCount != 1) throw new IllegalArgumentException("single_choice_image must have exactly 1 correct option");
                boolean allHaveImage = dto.getOptions().stream().allMatch(o -> o.getImageUrl() != null && !o.getImageUrl().isBlank());
                if (!allHaveImage) throw new IllegalArgumentException("single_choice_image options must include imageUrl");
            }
            case "multiple_choice_single", "listen" -> {
                if (optCount < Math.max(2, coalesce(questionType.getMinOptions(), 2))) throw new IllegalArgumentException("At least 2 options required");
                if (correctCount != 1) throw new IllegalArgumentException("Exactly 1 correct option required");
            }
            case "multiple_choice_multi" -> {
                if (optCount < Math.max(2, coalesce(questionType.getMinOptions(), 2))) throw new IllegalArgumentException("At least 2 options required");
                if (correctCount < Math.max(1, coalesce(questionType.getMinCorrect(), 1))) throw new IllegalArgumentException("At least 1 correct option required");
            }
            case "fill_in_blank" -> {
                if (ansCount < 1) throw new IllegalArgumentException("fill_in_blank requires at least 1 answer");
                if (dto.getAnswers() != null && dto.getAnswers().stream().anyMatch(a -> a.getPosition() != null)) {
                    int n = (int) dto.getAnswers().stream().filter(a -> a.getPosition() != null).count();
                    ensurePositionsAre1ToN(dto.getAnswers().stream().map(AnswerCreateDto::getPosition).collect(Collectors.toList()), n,
                            "answers positions for fill_in_blank must be 1..K without duplicates");
                }
            }
            case "matching" -> {
                if (optCount < 2 || optCount % 2 != 0) throw new IllegalArgumentException("matching requires an even number of options >= 2");
            }
            case "speaking" -> {
                // No strict requirement; optionally needs audioUrl or reference answer
            }
            default -> { /* no-op */ }
        }
    }

    private void validateCounts(QuestionType type, List<QuestionOption> options, List<QuestionAnswer> answers) {
        int optCount = options == null ? 0 : options.size();
        int correctCount = options == null ? 0 : (int) options.stream().filter(QuestionOption::isCorrect).count();
        int ansCount = answers == null ? 0 : answers.size();

        if (type.getMinOptions() != null && type.getMinOptions() > 0 && optCount < type.getMinOptions()) {
            throw new IllegalStateException("Need at least %d options".formatted(type.getMinOptions()));
        }
        if (type.getMinCorrect() != null && type.getMinCorrect() > 0 && correctCount < type.getMinCorrect()) {
            throw new IllegalStateException("Need at least %d correct options".formatted(type.getMinCorrect()));
        }
        if (type.getMaxCorrect() != null && type.getMaxCorrect() > 0 && correctCount > type.getMaxCorrect()) {
            throw new IllegalStateException("At most %d correct options".formatted(type.getMaxCorrect()));
        }

        String code = type.getCode();
        if (code == null) return;
        switch (code) {
            case "text_input" -> {
                if (optCount > 0) throw new IllegalStateException("text_input should not have options");
                if (ansCount < 1) throw new IllegalStateException("text_input requires at least 1 answer");
            }
            case "reorder_words" -> {
                if (ansCount != optCount) throw new IllegalStateException("reorder_words answers must match options count");
                ensurePositionsAre1ToN(answers.stream().map(QuestionAnswer::getPosition).collect(Collectors.toList()), optCount,
                        "answers positions for reorder_words must be 1..N without duplicates");
            }
            case "single_choice_image", "multiple_choice_single", "listen" -> {
                if (correctCount != 1) throw new IllegalStateException("Exactly 1 correct option required");
            }
            case "matching" -> {
                if (optCount < 2 || optCount % 2 != 0) throw new IllegalStateException("matching requires an even number of options >= 2");
            }
            default -> { /* no-op */ }
        }
    }

    private void ensurePositionsAre1ToN(List<Integer> positions, int n, String message) {
        if (positions == null || n <= 0) return;
        List<Integer> pos = positions.stream().filter(p -> p != null).collect(Collectors.toList());
        if (pos.size() != n) throw new IllegalArgumentException(message);
        boolean ok = pos.stream().distinct().count() == n && pos.stream().allMatch(p -> p >= 1 && p <= n);
        if (!ok) throw new IllegalArgumentException(message);
    }

    private static Integer coalesce(Integer value, Integer fallback) {
        return value != null ? value : fallback;
    }
}
