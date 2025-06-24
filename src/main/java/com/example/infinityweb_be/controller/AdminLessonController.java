// src/main/java/com/example/infinityweb_be/controller/AdminLessonController.java
package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.dto.LessonDto;
import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class AdminLessonController {

    private final LessonService lessonService;
    private final UserRepository userRepository;

    // GET /api/lessons?moduleId=...
    @GetMapping
    public List<LessonDto> getByModuleId(@RequestParam Integer moduleId) {
        return lessonService.getByModuleId(moduleId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // GET /api/lessons/{id}
    @GetMapping("/{id}")
    public LessonDto getById(@PathVariable Integer id) {
        Lesson lesson = lessonService.findById(id);
        return toDto(lesson);
    }

    // POST /api/lessons
    @PostMapping
    public LessonDto create(@RequestBody LessonDto dto, JwtAuthenticationToken token) {
        String email = token.getName();
        int adminId = userRepository.findByEmail(email).orElseThrow().getId();
        Lesson created = lessonService.createFromDto(dto, adminId);
        return toDto(created);
    }

    // PUT /api/lessons/{id}
    @PutMapping("/{id}")
    public LessonDto update(@PathVariable Integer id,
                            @RequestBody LessonDto dto,
                            JwtAuthenticationToken token) {
        String email = token.getName();
        int adminId = userRepository.findByEmail(email).orElseThrow().getId();
        Lesson updated = lessonService.updateFromDto(id, dto, adminId);
        return toDto(updated);
    }

    // DELETE /api/lessons/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        lessonService.delete(id);
    }

    // Mapping entity → DTO
// Mapping entity → DTO
// Sử dụng constructor all-args
    private LessonDto toDto(Lesson lesson) {
        return new LessonDto(
                lesson.getId(),
                lesson.getName(),
                lesson.getDescription(),
                lesson.getModule().getId(),
                lesson.getModule().getName(),
                lesson.getContent(),
                lesson.getType(),
                lesson.getOrder(),
                lesson.getDuration(),
                lesson.getStatus(),
                // Lấy id thay vì User object
                lesson.getCreatedBy().getId(),
                lesson.getCreatedAt(),
                // updatedBy có thể null nếu chưa cập nhật
                lesson.getUpdatedBy() != null ? lesson.getUpdatedBy().getId() : null,
                lesson.getUpdatedAt()
        );
    }


}
