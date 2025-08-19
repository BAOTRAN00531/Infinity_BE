package com.example.infinityweb_be.service.Enrollment;

import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.course.Course;
import com.example.infinityweb_be.domain.enrollment.Enrollment;
import com.example.infinityweb_be.repository.enrollment.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

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
}