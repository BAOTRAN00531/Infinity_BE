package com.example.infinityweb_be.controller.student.Course;

import com.example.infinityweb_be.domain.dto.student.LearningCourseDto;
import com.example.infinityweb_be.service.UserService;
import com.example.infinityweb_be.service.course.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
@RequestMapping("/api/student/course")
@RequiredArgsConstructor
public class StudentCourseController {

    private final CourseService courseService;
    private final UserService userService;

    @GetMapping("/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<LearningCourseDto> getPurchasedCourseDetails(
            @PathVariable Integer courseId,
            Principal principal
    ) {
        Integer userId = userService.getUserIdFromPrincipal(principal);
        // Logic kiểm tra đã ở trong service
        LearningCourseDto course = courseService.getCourseForStudent(courseId, userId);
        return ResponseEntity.ok(course);
    }
}