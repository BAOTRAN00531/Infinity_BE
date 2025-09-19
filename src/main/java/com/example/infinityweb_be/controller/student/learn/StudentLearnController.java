package com.example.infinityweb_be.controller.student.learn;

import com.example.infinityweb_be.domain.dto.LessonDto;
import com.example.infinityweb_be.domain.dto.learn.LearnPathDto;
import com.example.infinityweb_be.domain.dto.learn.LessonProgressDto;
import com.example.infinityweb_be.service.UserService;
import com.example.infinityweb_be.service.learn.StudentLearnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/student/learn")
@RequiredArgsConstructor
public class StudentLearnController {

    private final StudentLearnService studentLearnService;
    private final UserService userService;

    @GetMapping("/path/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<LearnPathDto> getLearnPath(@PathVariable Integer courseId,
                                                     Principal principal) {
        Integer userId = userService.getUserIdFromPrincipal(principal);
        LearnPathDto learnPath = studentLearnService.getLearnPathForStudent(courseId, userId);
        return ResponseEntity.ok(learnPath);
    }

    @GetMapping("/lessons/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<LessonDto>> getLessonsWithProgress(
            @PathVariable Integer courseId,
            Principal principal
    ) {
        Integer userId = userService.getUserIdFromPrincipal(principal);
        List<LessonDto> lessons = studentLearnService.getLessonsWithProgressForStudent(courseId, userId);
        return ResponseEntity.ok(lessons);
    }

    @PostMapping("/lesson/{lessonId}/progress")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> updateLessonProgress(
            @PathVariable Integer lessonId,
            @RequestBody LessonProgressDto progressDto,
            Principal principal
    ) {
        Integer userId = userService.getUserIdFromPrincipal(principal);
        studentLearnService.updateLessonProgress(lessonId, userId, progressDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/next-lesson/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<LessonDto> getNextLesson(
            @PathVariable Integer courseId,
            Principal principal
    ) {
        Integer userId = userService.getUserIdFromPrincipal(principal);
        LessonDto nextLesson = studentLearnService.getNextLessonForStudent(courseId, userId);
        return ResponseEntity.ok(nextLesson);
    }
}