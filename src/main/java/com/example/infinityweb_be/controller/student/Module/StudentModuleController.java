package com.example.infinityweb_be.controller.student.Module;

import com.example.infinityweb_be.domain.dto.modules.LearningModuleDto;
import com.example.infinityweb_be.service.UserService;
import com.example.infinityweb_be.service.module.LearningModuleService;
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
@RequestMapping("/api/student/module")
@RequiredArgsConstructor
public class StudentModuleController {

    private final LearningModuleService learningModuleService;
    private final UserService userService;

    @GetMapping("/by-course/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<LearningModuleDto>> getModulesByCourse(
            @PathVariable Integer courseId,
            Principal principal
    ) {
        Integer userId = userService.getUserIdFromPrincipal(principal);
        // Logic kiểm tra đã ở trong service
        List<LearningModuleDto> modules = learningModuleService.getModulesByCourseForStudent(courseId, userId);
        return ResponseEntity.ok(modules);
    }
}