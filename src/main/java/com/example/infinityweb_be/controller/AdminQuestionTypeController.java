package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.QuestionType;
import com.example.infinityweb_be.service.QuestionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-types")
@RequiredArgsConstructor
public class AdminQuestionTypeController {

    private final QuestionTypeService service;

    @GetMapping
    public List<QuestionType> getAll() {
        return service.getAll();
    }

    @PostMapping
    public QuestionType create(@RequestBody QuestionType type) {
        return service.create(type);
    }

    @PutMapping("/{id}")
    public QuestionType update(@PathVariable Integer id, @RequestBody QuestionType type) {
        return service.update(id, type);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}