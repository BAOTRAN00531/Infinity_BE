package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.dto.LessonDto;
import com.example.infinityweb_be.domain.dto.question.admin.QuestionCreateDto;
import com.example.infinityweb_be.domain.dto.question.admin.QuestionResponseDto;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.request.QuestionUpsertRequest;
import com.example.infinityweb_be.service.lesson.LessonService;
import com.example.infinityweb_be.service.question.QuestionService;
import com.example.infinityweb_be.service.question.managment.QuestionAudioService;
import com.example.infinityweb_be.service.question.managment.QuestionCommandService;
import com.example.infinityweb_be.service.question.managment.QuestionQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
public class AdminQuestionController {
    private final QuestionCommandService questionCommandService;
    private final QuestionQueryService questionQueryService;
    private final QuestionAudioService questionAudioService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid QuestionUpsertRequest req) {
        Long id = questionCommandService.create(req);
        return ResponseEntity.ok(java.util.Map.of("id", id)); // ← luôn ra {"id": 123}
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid QuestionUpsertRequest req) {
        return ResponseEntity.ok(questionCommandService.update(id, req));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        questionCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(questionQueryService.getDetail(id));
    }
    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<?> listByLesson(
            @PathVariable Integer lessonId,
            @RequestParam(required = false) Integer questionTypeId
    ) {
        if (questionTypeId != null) {
            return ResponseEntity.ok(questionQueryService.listByLessonAndType(lessonId, questionTypeId));
        }
        return ResponseEntity.ok(questionQueryService.listByLesson(lessonId));
    }
    @PostMapping("/{id}/generate-audio")
    public ResponseEntity<?> generateAudio(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> body
    ) {
        // body có thể chứa voice override: { "engine": "...", "voice": "...", "speed": 1.0, ... }
        String url = questionAudioService.generateAudio(id, body);
        return ResponseEntity.ok(Map.of("audioUrl", url));
    }
}