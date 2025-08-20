
// StudentDashboardController.java
// src/main/java/com/example/infinityweb_be/controller/student/StudentDashboardController.java
package com.example.infinityweb_be.controller.student;

import com.example.infinityweb_be.domain.dto.student.StudentCourseProgressDto;
import com.example.infinityweb_be.repository.CourseRepository;

import com.example.infinityweb_be.service.UserService;
import com.example.infinityweb_be.service.course.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/student/dashboard")
@RequiredArgsConstructor
public class StudentDashboardController {

    private final CourseService courseService; // ✅ thay vì repository
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public List<StudentCourseProgressDto> getDashboardCourses(Principal principal) {
        Integer userId = userService.getUserIdFromPrincipal(principal);
        return courseService.getStudentDashboardCourses(userId);
    }
}
