// QuestionServiceImpl.java
package com.example.infinityweb_be.service.question;

import com.example.infinityweb_be.domain.*;
import com.example.infinityweb_be.domain.dto.question.*;
import com.example.infinityweb_be.domain.mapper.QuestionMapper;
import com.example.infinityweb_be.repository.*;
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

    //    private final QuestionMapper mapper;
    @Autowired
    private QuestionMapper mapper;

    public List<QuestionResponseDto> getAll() {
        List<Question> questions = questionRepo.findAll();
        return questions.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @PersistenceContext
    private EntityManager em;

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

    @Override
    public QuestionResponseDto create(QuestionCreateDto dto, Integer adminId) {
        setSessionContext(adminId);

        // Ánh xạ DTO sang entity
        Question q = mapper.toEntity(dto);

        // Gán metadata
        q.setCreatedBy(userRepo.getReferenceById(adminId));
        q.setCreatedAt(LocalDateTime.now());

        // Gán các quan hệ
        if (dto.getCourseId() != null) {
            Course course = courseRepo.findById(dto.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));
            q.setCourse(course);
        }

        Lesson lesson = lessonRepo.findById(dto.getLessonId())
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found: "));
        q.setLesson(lesson);

        QuestionType questionType = typeRepo.findById(dto.getQuestionTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Question type not found: " ));
        q.setQuestionType(questionType);

        // Lưu câu hỏi trước để có ID
        Question saved = questionRepo.save(q);

//// ✅ Lưu options nếu có
//        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
//            dto.getOptions().forEach(optDto -> {
//                QuestionOption opt = optDto.toEntity(saved); // ✅ Đúng với thiết kế DTO
//                optionRepo.save(opt);
//            });
//        }
//
//// ✅ Lưu answers nếu có
//        if (dto.getAnswers() != null && !dto.getAnswers().isEmpty()) {
//            dto.getAnswers().forEach(ansDto -> {
//                QuestionAnswer ans = ansDto.toEntity(saved); // ✅ Đúng với thiết kế DTO
//                answerRepo.save(ans);
//            });
//        }


        return mapper.toResponseDto(saved);
    }




    @Override
    public QuestionResponseDto update(Integer id, QuestionCreateDto dto, Integer adminId) {
        setSessionContext(adminId);

        Question q = questionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found: " + id));

        // Cập nhật trường đơn giản
        q.setContent(dto.getQuestionText());
        q.setLevel(dto.getDifficulty());
        q.setScore(dto.getPoints());
        q.setUpdatedAt(LocalDateTime.now());
        q.setUpdatedBy(userRepo.getReferenceById(adminId));

        // Cập nhật quan hệ
        if (dto.getCourseId() != null) {
            Course course = courseRepo.findById(dto.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found: " + dto.getCourseId()));
            q.setCourse(course);
        } else {
            q.setCourse(null);
        }

        Lesson lesson = lessonRepo.findById(dto.getLessonId())
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + dto.getLessonId()));
        q.setLesson(lesson);

        QuestionType questionType = typeRepo.findById(dto.getQuestionTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Question type not found: " + dto.getQuestionTypeId()));
        q.setQuestionType(questionType);

        // Cập nhật media nếu có
        if (dto.getMedia() != null) {
            q.setMediaUrl(dto.getMedia().getMediaUrl());
            q.setAudioUrl(dto.getMedia().getAudioUrl());
            q.setVideoUrl(dto.getMedia().getVideoUrl());
        } else {
            q.setMediaUrl(null);
            q.setAudioUrl(null);
            q.setVideoUrl(null);
        }

        // Lưu lại Question đã cập nhật
        Question updated = questionRepo.save(q);

        // -----------------------------------
        // 1. Xoá toàn bộ options & answers cũ
        // -----------------------------------
        optionRepo.deleteByQuestion_Id(id);
        answerRepo.deleteByQuestion_Id(id);

        // -----------------------------------
        // 2. Lưu mới options & answers mới
        // -----------------------------------
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            dto.getOptions().forEach(optDto -> {
                QuestionOption opt = optDto.toEntity(updated);
                optionRepo.save(opt);
            });
        }

        if (dto.getAnswers() != null && !dto.getAnswers().isEmpty()) {
            dto.getAnswers().forEach(ansDto -> {
                QuestionAnswer ans = ansDto.toEntity(updated);
                answerRepo.save(ans);
            });
        }

        return mapper.toResponseDto(updated);
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
