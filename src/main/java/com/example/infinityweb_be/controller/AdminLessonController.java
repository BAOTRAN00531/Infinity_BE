// src/main/java/com/example/infinityweb_be/controller/AdminLessonController.java
package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.dto.LessonDto;
import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class AdminLessonController {

    private final LessonService lessonService;
    private final UserRepository userRepository;

    @Autowired
    LessonRepository lessonRepository;


    @GetMapping("/max-order")
    public ResponseEntity<Map<String, Integer>> getMaxOrder(@RequestParam Integer moduleId) {
        int max = lessonRepository.findMaxOrderByModule_Id(moduleId);
        return ResponseEntity.ok(Map.of("maxOrder", max));
    }



    // GET /api/lessons/{id}
    @GetMapping("/{id}")
    public LessonDto getById(@PathVariable Integer id) {
        Lesson lesson = lessonService.findById(id);
        return toDto(lesson);
    }

    @GetMapping
    public List<LessonDto> listLessons(@RequestParam(required = false) Integer moduleId) {
        List<Lesson> lessons;
        if (moduleId != null && moduleId > 0) {
            lessons = lessonService.getByModuleId(moduleId);
        } else {
            lessons = lessonService.getAllLessons();
        }
        return lessons.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
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
                lesson.getContent(),
                lesson.getType(),
                lesson.getOrderIndex(),             // <-- dùng getOrderIndex()
                lesson.getDuration(),
                lesson.getStatus(),
                lesson.getModule().getId(),
                lesson.getModule().getName(),
                lesson.getCreatedBy().getId(),
                lesson.getCreatedAt(),
                lesson.getUpdatedBy() != null ? lesson.getUpdatedBy().getId() : null,
                lesson.getUpdatedAt()
        );
    }


}
