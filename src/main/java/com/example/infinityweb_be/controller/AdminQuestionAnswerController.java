// src/main/java/com/example/infinityweb_be/controller/AdminQuestionAnswerController.java
package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.dto.question.admin.AnswerCreateDto;
import com.example.infinityweb_be.domain.dto.question.admin.AnswerResponseDto;
import com.example.infinityweb_be.service.question.QuestionAnswerService;
import com.example.infinityweb_be.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-answers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminQuestionAnswerController {
    private final AuthHelper authHelper;
    private final QuestionAnswerService service;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerResponseDto create(
            @Valid @RequestBody AnswerCreateDto dto,
            JwtAuthenticationToken token
    ) {
        int adminId = authHelper.getCurrentUserId(token);
        return service.create(dto, adminId);
    }

    @PutMapping("/{id}")
    public AnswerResponseDto update(
            @PathVariable Integer id,
            @Valid @RequestBody AnswerCreateDto dto,
            JwtAuthenticationToken token
    ) {
        int adminId = authHelper.getCurrentUserId(token);
        return service.update(id, dto, adminId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping
    public List<AnswerResponseDto> getByQuestion(@RequestParam Integer questionId) {
        return service.getByQuestionId(questionId);
    }
}

