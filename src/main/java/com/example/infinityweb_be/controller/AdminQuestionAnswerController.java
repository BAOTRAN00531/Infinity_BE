package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.QuestionAnswer;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.QuestionAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-answers")
@RequiredArgsConstructor
public class AdminQuestionAnswerController {

    private final QuestionAnswerService service;
    private final UserRepository userRepository;

    @PostMapping
    public QuestionAnswer create(@RequestBody QuestionAnswer answer, JwtAuthenticationToken token) {
        int adminId = userRepository.findByEmail(token.getName()).orElseThrow().getId();
        return service.create(answer, adminId);
    }

    @PutMapping("/{id}")
    public QuestionAnswer update(@PathVariable Integer id, @RequestBody QuestionAnswer answer, JwtAuthenticationToken token) {
        int adminId = userRepository.findByEmail(token.getName()).orElseThrow().getId();
        return service.update(id, answer, adminId);
    }

    @GetMapping
    public List<QuestionAnswer> getByQuestion(@RequestParam Integer questionId) {
        return service.getByQuestionId(questionId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}