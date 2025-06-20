package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class AdminLessonController {

    private final LessonService lessonService;
    private final UserRepository userRepository;

    @GetMapping
    public List<Lesson> getByModuleId(@RequestParam Integer moduleId) {
        return lessonService.getByModuleId(moduleId);
    }

    @PostMapping
    public Lesson create(@RequestBody Lesson lesson, JwtAuthenticationToken token) {
        String email = token.getName();
        int adminId = userRepository.findByEmail(email).orElseThrow().getId();
        return lessonService.create(lesson, adminId);
    }

    @PutMapping("/{id}")
    public Lesson update(@PathVariable Integer id, @RequestBody Lesson lesson, JwtAuthenticationToken token) {
        String email = token.getName();
        int adminId = userRepository.findByEmail(email).orElseThrow().getId();
        return lessonService.update(id, lesson, adminId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        lessonService.delete(id);
    }
}
