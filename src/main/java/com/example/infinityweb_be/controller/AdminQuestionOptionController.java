// src/main/java/com/example/infinityweb_be/controller/AdminQuestionOptionController.java
package com.example.infinityweb_be.controller;


import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.dto.question.OptionCreateDto;
import com.example.infinityweb_be.domain.dto.question.OptionResponseDto;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.question.QuestionOptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-options")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminQuestionOptionController {
    private final QuestionOptionService service;
    private final UserRepository         userRepository;
    private final AuthHelper authHelper;


    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<OptionResponseDto> createBatch(
            @Valid @RequestBody List<OptionCreateDto> dtos,
            JwtAuthenticationToken token
    ) {
        int adminId = authHelper.getCurrentUserId(token);
        return service.createAll(dtos, adminId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OptionResponseDto create(
            @Valid @RequestBody OptionCreateDto dto,
            JwtAuthenticationToken token
    ) {
        int adminId = authHelper.getCurrentUserId(token);
        return service.create(dto, adminId);
    }

    @PutMapping("/{id}")
    public OptionResponseDto update(
            @PathVariable Integer id,
            @Valid @RequestBody OptionCreateDto dto,
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
    public List<OptionResponseDto> getByQuestion(
            @RequestParam Integer questionId
    ) {
        return service.getByQuestionId(questionId);
    }
}
