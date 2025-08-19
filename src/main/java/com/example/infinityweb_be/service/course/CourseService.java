package com.example.infinityweb_be.service.course;

import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.domain.course.Course;
import com.example.infinityweb_be.domain.dto.CourseDto;
import com.example.infinityweb_be.domain.dto.LessonDto;
import com.example.infinityweb_be.domain.dto.modules.LearningModuleDto;
import com.example.infinityweb_be.domain.dto.order.OrderStatus;
import com.example.infinityweb_be.domain.dto.student.LearningCourseDto;
import com.example.infinityweb_be.repository.CourseRepository;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.repository.LearningModuleRepository;
import com.example.infinityweb_be.repository.enrollment.EnrollmentRepository;
import com.example.infinityweb_be.repository.order.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.infinityweb_be.domain.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final LearningModuleRepository learningModuleRepository;
    private final LessonRepository lessonRepository;
    private final OrderRepository orderRepository;
    private final EnrollmentRepository enrollmentRepository;

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

        if (course.getPrice() == null) {
            throw new IllegalArgumentException("Price must not be null.");
        }

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

        existing.setThumbnail(updatedCourse.getThumbnail());
        existing.setPrice(updatedCourse.getPrice());

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
                course.getStatus(),
                course.getThumbnail()
        );
    }


    public CourseDto getDtoById(int id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));
        return toDto(course);
    }

    public List<Course> getCoursesByLanguage(Integer languageId) {
        return courseRepository.findByLanguageId(languageId);
    }


//    ===========HIEN THI COURSE STUDENT=============

    public LearningCourseDto getCourseForStudent(Integer courseId, Integer userId) {
        // 1. Kiểm tra học viên đã được ghi danh vào khóa học chưa
        boolean isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
        if (!isEnrolled) {
            throw new AccessDeniedException("Bạn chưa mua khóa học này.");
        }

        // 2. Lấy thông tin khóa học
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học."));

        // 3. Lấy danh sách module
        List<LearningModuleDto> moduleDtos = learningModuleRepository.findByCourseId(courseId)
                .stream()
                .map(module -> {
                    long partsCount = lessonRepository.countByModule_Id(module.getId());

                    return new LearningModuleDto(
                            module.getId(),
                            module.getName(),
                            module.getDescription(),
                            course.getId(),
                            course.getName(),
                            module.getOrder(),
                            module.getDuration(),
                            module.getStatus(),
                            partsCount
                    );
                })
                .collect(Collectors.toList());

        // 4. Trả về DTO tổng hợp
        return new LearningCourseDto(
                course.getId(),
                course.getName(),
                course.getThumbnail(),
                moduleDtos
        );
    }

}