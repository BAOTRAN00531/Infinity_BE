package com.example.infinityweb_be.service.question;

import com.example.infinityweb_be.domain.dto.question.*;
import java.util.List;

public interface QuestionService {
    QuestionResponseDto getById(Integer id);
    List<QuestionResponseDto> getByLessonId(Integer lessonId);
    QuestionResponseDto create(QuestionCreateDto dto, Integer adminId);



    QuestionResponseDto update(Integer id, QuestionCreateDto dto, Integer adminId);
    void delete(Integer id);
    void validateQuestionBeforeUse(Integer questionId);

    List<QuestionResponseDto> getAll();

    ;
}

//package com.example.infinityweb_be.service;
//
//import com.example.infinityweb_be.domain.Question;
//import com.example.infinityweb_be.domain.QuestionAnswer;
//import com.example.infinityweb_be.domain.QuestionOption;
//import com.example.infinityweb_be.domain.QuestionType;
//import com.example.infinityweb_be.domain.dto.AnswerDto;
//import com.example.infinityweb_be.domain.dto.OptionDto;
//import com.example.infinityweb_be.domain.dto.QuestionDto;
//import com.example.infinityweb_be.repository.LessonRepository;
//import com.example.infinityweb_be.repository.QuestionAnswerRepository;
//import com.example.infinityweb_be.repository.QuestionOptionRepository;
//import com.example.infinityweb_be.repository.QuestionRepository;
//import com.example.infinityweb_be.repository.QuestionTypeRepository;
//import com.example.infinityweb_be.repository.UserRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import jakarta.persistence.EntityNotFoundException;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class QuestionService {
//    private final QuestionRepository questionRepository;
//    private final QuestionOptionRepository questionOptionRepository;
//    private final QuestionAnswerRepository questionAnswerRepository;
//    private final QuestionTypeRepository questionTypeRepository;
//    private final LessonRepository lessonRepository;
//    private final UserRepository userRepository;
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    private void setSessionContext(int userId) {
//        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :userId")
//                .setParameter("userId", userId)
//                .executeUpdate();
//    }
//
//    /** Lấy detail đầy đủ của một question (kèm options & answers) */
//    @Transactional(Transactional.TxType.REQUIRED)
//    public QuestionDto getDetail(Integer id) {
//        Question q = questionRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Question not found id=" + id));
//        List<QuestionOption> opts = questionOptionRepository.findByQuestionId(id);
//        List<QuestionAnswer> ans = questionAnswerRepository.findByQuestionId(id);
//        return mapToDto(q, opts, ans);
//    }
//
//    /** Tạo mới một question cùng toàn bộ options/answers trong 1 transaction */
//    @Transactional(Transactional.TxType.REQUIRED)
//    public QuestionDto createFull(QuestionDto dto, int adminId) {
//        setSessionContext(adminId);
//
//        // 1) Tạo question
//        QuestionType qt = questionTypeRepository.findByCode(dto.getQuestionTypeCode())
//                .orElseThrow(() -> new EntityNotFoundException("QuestionType not found code=" + dto.getQuestionTypeCode()));
//
//        Question q = new Question();
//        q.setContent(dto.getContent());
//        q.setQuestionType(qt);
//        q.setLevel(dto.getDifficulty());
//        q.setScore(dto.getScore());
//        q.setMediaUrl(dto.getMediaUrl());
//        q.setAudioUrl(dto.getAudioUrl());
//        q.setVideoUrl(dto.getVideoUrl());
//        q.setLesson(lessonRepository.findById(dto.getLessonId())
//                .orElseThrow(() -> new EntityNotFoundException("Lesson not found id=" + dto.getLessonId())));
//        q.setCreatedBy(userRepository.findById(adminId).orElseThrow());
//        q.setCreatedAt(LocalDateTime.now());
//        q = questionRepository.save(q);
//
//        // 2) Tạo options (nếu có)
//        if (dto.getOptions() != null) {
//            for (OptionDto o : dto.getOptions()) {
//                QuestionOption opt = new QuestionOption();
//                opt.setQuestion(q);
//                opt.setOptionText(o.getOptionText());
//                opt.setCorrect(o.isCorrect());
//                opt.setPosition(o.getPosition());
//                opt.setImageUrl(o.getImageUrl());
//                questionOptionRepository.save(opt);
//            }
//        }
//
//        // 3) Tạo answers (nếu có)
//        if (dto.getAnswers() != null) {
//            for (AnswerDto a : dto.getAnswers()) {
//                QuestionAnswer ansEnt = new QuestionAnswer();
//                ansEnt.setQuestion(q);
//                ansEnt.setAnswerText(a.getAnswerText());
//                ansEnt.setCaseSensitive(a.isCaseSensitive());
//                ansEnt.setPosition(a.getPosition());
//                questionAnswerRepository.save(ansEnt);
//            }
//        }
//
//        return getDetail(q.getId());
//    }
//
//    /** Cập nhật toàn bộ question + options/answers */
//    @Transactional(Transactional.TxType.REQUIRED)
//    public QuestionDto updateFull(Integer id, QuestionDto dto, int adminId) {
//        setSessionContext(adminId);
//
//        // 1) Cập nhật question
//        Question q = questionRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Question not found id=" + id));
//
//        QuestionType qt = questionTypeRepository.findByCode(dto.getQuestionTypeCode())
//                .orElseThrow(() -> new EntityNotFoundException("QuestionType not found code=" + dto.getQuestionTypeCode()));
//
//        q.setContent(dto.getContent());
//        q.setQuestionType(qt);
//        q.setLevel(dto.getDifficulty());
//        q.setScore(dto.getScore());
//        q.setMediaUrl(dto.getMediaUrl());
//        q.setAudioUrl(dto.getAudioUrl());
//        q.setVideoUrl(dto.getVideoUrl());
//        q.setLesson(lessonRepository.findById(dto.getLessonId())
//                .orElseThrow(() -> new EntityNotFoundException("Lesson not found id=" + dto.getLessonId())));
//        q.setUpdatedBy(userRepository.findById(adminId).orElseThrow());
//        q.setUpdatedAt(LocalDateTime.now());
//        questionRepository.save(q);
//
//        // 2) Xóa options & answers cũ
//        questionOptionRepository.deleteByQuestionId(id);
//        questionAnswerRepository.deleteByQuestionId(id);
//
//        // 3) Tạo lại options & answers từ DTO
//        if (dto.getOptions() != null) {
//            for (OptionDto o : dto.getOptions()) {
//                QuestionOption opt = new QuestionOption();
//                opt.setQuestion(q);
//                opt.setOptionText(o.getOptionText());
//                opt.setCorrect(o.isCorrect());
//                opt.setPosition(o.getPosition());
//                opt.setImageUrl(o.getImageUrl());
//                questionOptionRepository.save(opt);
//            }
//        }
//        if (dto.getAnswers() != null) {
//            for (AnswerDto a : dto.getAnswers()) {
//                QuestionAnswer ansEnt = new QuestionAnswer();
//                ansEnt.setQuestion(q);
//                ansEnt.setAnswerText(a.getAnswerText());
//                ansEnt.setCaseSensitive(a.isCaseSensitive());
//                ansEnt.setPosition(a.getPosition());
//                questionAnswerRepository.save(ansEnt);
//            }
//        }
//
//        return getDetail(id);
//    }
//
//    /** Lấy danh sách question của một lesson (chỉ trả entity Question) */
//    @Transactional(Transactional.TxType.SUPPORTS)
//    public List<Question> getByLessonId(Integer lessonId) {
//        return questionRepository.findByLessonId(lessonId);
//    }
//
//    /** Xóa question (chỉ xoá record Question; cascade option/answer nếu FK ON DELETE CASCADE) */
//    @Transactional(Transactional.TxType.REQUIRED)
//    public void delete(Integer id) {
//        questionRepository.deleteById(id);
//    }
//
//    /** Kiểm tra trước khi publish/preview */
//    @Transactional(Transactional.TxType.SUPPORTS)
//    public void validateQuestionBeforeUse(Integer questionId) {
//        List<QuestionOption> options = questionOptionRepository.findByQuestionId(questionId);
//        if (options.size() < 2) {
//            throw new IllegalStateException("Câu hỏi phải có ít nhất 2 lựa chọn!");
//        }
//    }
//
//    /** Helper map entity → DTO */
//    private QuestionDto mapToDto(Question q,
//                                 List<QuestionOption> opts,
//                                 List<QuestionAnswer> ans) {
//        QuestionDto dto = new QuestionDto();
//        dto.setId(q.getId());
//        dto.setContent(q.getContent());
//        dto.setLessonId(q.getLesson().getId());
//        dto.setQuestionTypeCode(q.getQuestionType().getCode());
//        dto.setDifficulty(q.getLevel());
//        dto.setScore(q.getScore());
//        dto.setMediaUrl(q.getMediaUrl());
//        dto.setAudioUrl(q.getAudioUrl());
//        dto.setVideoUrl(q.getVideoUrl());
//
//        dto.setOptions(
//                opts.stream().map(o -> new OptionDto(
//                        o.getId(),
//                        o.getOptionText(),
//                        o.isCorrect(),
//                        o.getPosition(),
//                        o.getImageUrl()
//                )).collect(Collectors.toList())
//        );
//
//        dto.setAnswers(
//                ans.stream().map(a -> new AnswerDto(
//                        a.getId(),
//                        a.getAnswerText(),
//                        a.isCaseSensitive(),
//                        a.getPosition()
//                )).collect(Collectors.toList())
//        );
//
//        return dto;
//    }
//}
