package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.dto.LessonDto;
import com.example.infinityweb_be.domain.dto.question.QuestionCreateDto;
import com.example.infinityweb_be.domain.dto.question.QuestionResponseDto;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.lesson.LessonService;
import com.example.infinityweb_be.service.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminQuestionController {
    private final QuestionService questionService;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final AuthHelper authHelper;
    private final LessonService lessonService;

    @GetMapping("/{id}")
    public QuestionResponseDto getById(@PathVariable Integer id) {
        return questionService.getById(id);
    }

    @GetMapping
    public List<QuestionResponseDto> getByLesson(@RequestParam Integer lessonId) {
        return questionService.getByLessonId(lessonId);
    }

    @GetMapping("/by-module/{moduleId}")
    public List<LessonDto> getLessonsByModule(@PathVariable Integer moduleId) {
        return lessonService.getByModuleIdDto(moduleId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<QuestionResponseDto> getAllQuestions() {
        return questionService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponseDto create(@Valid @RequestBody QuestionCreateDto dto,
                                      JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return questionService.create(dto, adminId);
    }

    @PutMapping("/{id}")
    public QuestionResponseDto update(@PathVariable Integer id,
                                      @Valid @RequestBody QuestionCreateDto dto,
                                      JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return questionService.update(id, dto, adminId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        questionService.delete(id);
    }

    @PostMapping("/{id}/validate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validate(@PathVariable Integer id) {
        questionService.validateQuestionBeforeUse(id);
    }
}
