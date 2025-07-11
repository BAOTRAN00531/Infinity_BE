package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.Course;
import com.example.infinityweb_be.repository.CourseRepository;
import com.example.infinityweb_be.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.example.infinityweb_be.domain.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Course> getAll() {
        return courseRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    @Transactional
    public Course create(Course course, int adminId) {
        // Set context cho trigger biết ai đang thao tác
        setSessionContext(adminId);

        User admin = userRepository.findById(adminId).orElseThrow();
        course.setCreatedBy(admin);
        course.setCreatedAt(LocalDateTime.now());
        return courseRepository.save(course);
    }
    @Transactional
    public Course update(int id, Course updatedCourse, int adminId) {
        setSessionContext(adminId);

        Course existing = courseRepository.findById(id).orElseThrow();
        existing.setName(updatedCourse.getName());
        existing.setDescription(updatedCourse.getDescription());
        existing.setLanguage(updatedCourse.getLanguage());
        existing.setLevel(updatedCourse.getLevel());           // ⬅️ Bổ sung
        existing.setDuration(updatedCourse.getDuration());     // ⬅️ Bổ sung
        existing.setStatus(updatedCourse.getStatus());
        existing.setUpdatedBy(userRepository.findById(adminId).orElseThrow());
        existing.setUpdatedAt(LocalDateTime.now());
        return courseRepository.save(existing);
    }

    public void delete(int id) {
        courseRepository.deleteById(id);
    }

    private void setSessionContext(int userId) {
        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }
}

