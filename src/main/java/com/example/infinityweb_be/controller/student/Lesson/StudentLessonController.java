package com.example.infinityweb_be.controller.student.Lesson;

import com.example.infinityweb_be.domain.dto.LessonDto;
import com.example.infinityweb_be.service.UserService;
import com.example.infinityweb_be.service.lesson.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/student/lesson")
@RequiredArgsConstructor
public class StudentLessonController {

    private final LessonService lessonService;
    private final UserService userService;

    @GetMapping("/by-module/{moduleId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<LessonDto>> getLessonsByModule(
            @PathVariable Integer moduleId,
            Principal principal
    ) {
        Integer userId = userService.getUserIdFromPrincipal(principal);
        List<LessonDto> lessons = lessonService.getLessonsByModuleForStudent(moduleId, userId);
        return ResponseEntity.ok(lessons);
    }
}
