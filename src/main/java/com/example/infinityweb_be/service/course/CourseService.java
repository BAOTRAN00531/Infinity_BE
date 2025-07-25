package com.example.infinityweb_be.service.course;

import com.example.infinityweb_be.domain.course.Course;
import com.example.infinityweb_be.domain.dto.CourseDto;
import com.example.infinityweb_be.repository.CourseRepository;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.repository.LearningModuleRepository;
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
    private final LearningModuleRepository learningModuleRepository;

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
        String oldStatus = existing.getStatus();
        String newStatus = updatedCourse.getStatus();
        existing.setName(updatedCourse.getName());
        existing.setDescription(updatedCourse.getDescription());
        existing.setLanguage(updatedCourse.getLanguage());
        existing.setLevel(updatedCourse.getLevel());           // ⬅️ Bổ sung
        existing.setDuration(updatedCourse.getDuration());     // ⬅️ Bổ sung
        existing.setStatus(newStatus);
        existing.setUpdatedBy(userRepository.findById(adminId).orElseThrow());
        existing.setUpdatedAt(LocalDateTime.now());

        // Nếu chuyển từ active sang inactive thì cập nhật tất cả module thành inactive
        if ("active".equalsIgnoreCase(oldStatus) && "inactive".equalsIgnoreCase(newStatus)) {
            learningModuleRepository.updateStatusByCourseId(existing.getId(), "inactive");
        }
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


    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .filter(course -> "ACTIVE".equalsIgnoreCase(course.getStatus()))
                .map(this::toDto)
                .toList();
    }

    public CourseDto toDto(Course course) {
        return new CourseDto(
                course.getId(),
                course.getName(),
                course.getDescription(),
                course.getDuration(),
                course.getLevel(),
                course.getLanguage() != null ? course.getLanguage().getName() : null,
                course.getPrice(),
                course.getStatus()
        );
    }


    public CourseDto getDtoById(int id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));
        return toDto(course);
    }


}