package com.example.infinityweb_be.service.Enrollment;

import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.course.Course;
import com.example.infinityweb_be.domain.enrollment.Enrollment;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.enrollment.EnrollmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;

    public void createEnrollment(User user, Course course) {
        // Check if user is already enrolled to prevent duplicates
        boolean isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(user.getId(), course.getId());
        if (!isEnrolled) {
            Enrollment enrollment = Enrollment.builder()
                    .user(user)
                    .course(course)
                    .enrolledAt(LocalDateTime.now())
                    .build();
            enrollmentRepository.save(enrollment);
        }
    }

    public void verifyEnrolledLesson(Integer userId, Integer lessonId) {
        // 1. Tìm lesson để lấy courseId
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new EntityNotFoundException("Lesson not found with id: " + lessonId));
        Integer courseId = lesson.getModule().getCourse().getId();
        // 2. Kiểm tra enrollment ở cấp độ COURSE
        boolean isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
        if (!isEnrolled) {
            throw new AccessDeniedException("Bạn chưa đăng ký khóa học chứa bài học này");
        }
    }
}