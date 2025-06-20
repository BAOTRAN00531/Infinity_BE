package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.QuestionOption;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.QuestionOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-options")
@RequiredArgsConstructor
public class AdminQuestionOptionController {

    private final QuestionOptionService service;
    private final UserRepository userRepository;

    @PostMapping
    public QuestionOption create(@RequestBody QuestionOption option, JwtAuthenticationToken token) {
        int adminId = userRepository.findByEmail(token.getName()).orElseThrow().getId();
        return service.create(option, adminId);
    }

    @PutMapping("/{id}")
    public QuestionOption update(@PathVariable Integer id, @RequestBody QuestionOption option, JwtAuthenticationToken token) {
        int adminId = userRepository.findByEmail(token.getName()).orElseThrow().getId();
        return service.update(id, option, adminId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping
    public List<QuestionOption> getByQuestion(@RequestParam Integer questionId) {
        return service.getByQuestionId(questionId);
    }
}
