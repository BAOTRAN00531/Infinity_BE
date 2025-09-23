package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.repository.question.QuestionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    
    private final QuestionTypeRepository questionTypeRepository;
    
    @GetMapping("/question-types")
    @PreAuthorize("permitAll()")
    public List<Object> getAllQuestionTypes() {
        return questionTypeRepository.findAll().stream()
                .map(qt -> new Object() {
                    public Integer id = qt.getId();
                    public String code = qt.getCode();
                    public String description = qt.getDescription();
                })
                .collect(java.util.stream.Collectors.toList());
    }
}
