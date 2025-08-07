// src/main/java/com/example/infinityweb_be/controller/student/StudentDashboardController.java
package com.example.infinityweb_be.controller.student;

import com.example.infinityweb_be.domain.dto.student.StudentCourseProgressDto;
import com.example.infinityweb_be.repository.CourseRepository;

import com.example.infinityweb_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/student/dashboard")
@RequiredArgsConstructor
public class StudentDashboardController {

//    private final EnrollmentRepository enrollmentRepository;
private final CourseRepository courseRepo;
    private final UserService userService;


    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public List<StudentCourseProgressDto> getDashboardCourses(Principal principal) {
        Integer userId = userService.getUserIdFromPrincipal(principal);
        return courseRepo.findStudentDashboardCourses(userId);

//        return enrollmentRepository.findStudentDashboardCourses(userId);
    }

    private Integer getUserIdFromPrincipal(Principal principal) {
        // TODO: bạn nên thay thế bằng cách lookup user từ email hoặc JWT
        // Giả sử tạm thời có UserService hỗ trợ như sau:
        return 1; // ví dụ cố định, bạn sẽ thay bằng real lookup
    }
}
