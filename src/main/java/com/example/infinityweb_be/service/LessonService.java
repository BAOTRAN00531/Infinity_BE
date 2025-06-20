package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.repository.LearningModuleRepository;
import com.example.infinityweb_be.repository.LessonRepository;
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
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LearningModuleRepository moduleRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Lesson create(Lesson lesson, int adminId) {
        setSessionContext(adminId);
        lesson.setCreatedAt(LocalDateTime.now());
        lesson.setCreatedBy(userRepository.findById(adminId).orElseThrow());
        return lessonRepository.save(lesson);
    }

    @Transactional
    public Lesson update(Integer id, Lesson newData, int adminId) {
        setSessionContext(adminId);
        Lesson lesson = lessonRepository.findById(id).orElseThrow();
        lesson.setName(newData.getName());
        lesson.setDescription(newData.getDescription());
        lesson.setModule(newData.getModule());
        lesson.setUpdatedBy(userRepository.findById(adminId).orElseThrow());
        lesson.setUpdatedAt(LocalDateTime.now());
        return lessonRepository.save(lesson);
    }

    public List<Lesson> getByModuleId(Integer moduleId) {
        return lessonRepository.findByModuleId(moduleId);
    }

    public void delete(Integer id) {
        lessonRepository.deleteById(id);
    }

    private void setSessionContext(int userId) {
        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
