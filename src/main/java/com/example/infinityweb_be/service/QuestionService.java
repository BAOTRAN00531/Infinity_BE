package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionOption;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.QuestionOptionRepository;
import com.example.infinityweb_be.repository.QuestionRepository;
import com.example.infinityweb_be.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionRepository questionRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Question create(Question question, int adminId) {
        setSessionContext(adminId);
        question.setCreatedAt(LocalDateTime.now());
        question.setCreatedBy(userRepository.findById(adminId).orElseThrow());
        return questionRepository.save(question);
    }

    @Transactional
    public Question update(Integer id, Question newData, int adminId) {
        setSessionContext(adminId);
        Question q = questionRepository.findById(id).orElseThrow();

        q.setContent(newData.getContent());
        q.setQuestionType(newData.getQuestionType());
        q.setLevel(newData.getLevel());
        q.setScore(newData.getScore());
        q.setMediaUrl(newData.getMediaUrl());
        q.setAudioUrl(newData.getAudioUrl());
        q.setVideoUrl(newData.getVideoUrl());
        q.setLesson(newData.getLesson());

        q.setUpdatedAt(LocalDateTime.now());
        q.setUpdatedBy(userRepository.findById(adminId).orElseThrow());

        return questionRepository.save(q);
    }

    public List<Question> getByLessonId(Integer lessonId) {
        return questionRepository.findByLessonId(lessonId);
    }

    public void delete(Integer id) {
        questionRepository.deleteById(id);
    }

    private void setSessionContext(int userId) {
        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }
    public void validateQuestionBeforeUse(Integer questionId) {
        List<QuestionOption> options = questionOptionRepository.findByQuestionId(questionId);

        if (options == null || options.size() < 2) {
            throw new IllegalStateException("❌ Câu hỏi này chưa có đủ 2 lựa chọn!");
        }
    }
}
