// src/main/java/com/example/infinityweb_be/controller/AdminQuestionTypeController.java
package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.QuestionType;
import com.example.infinityweb_be.service.question.QuestionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-types")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminQuestionTypeController {

    private final QuestionTypeService service;
    @GetMapping
    public List<QuestionType> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionType create(@RequestBody QuestionType type) {
        return service.create(type);
    }

    @PutMapping("/{id}")
    public QuestionType update(
            @PathVariable Integer id,
            @RequestBody QuestionType type
    ) {
        return service.update(id, type);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
