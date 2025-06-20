package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class AdminQuestionController {

    private final QuestionService questionService;
    private final UserRepository userRepository;

    @GetMapping
    public List<Question> getByLessonId(@RequestParam Integer lessonId) {
        return questionService.getByLessonId(lessonId);
    }

    @PostMapping
    public Question create(@RequestBody Question question, JwtAuthenticationToken token) {
        String email = token.getName();
        int adminId = userRepository.findByEmail(email).orElseThrow().getId();
        return questionService.create(question, adminId);
    }

    @PutMapping("/{id}")
    public Question update(@PathVariable Integer id, @RequestBody Question question, JwtAuthenticationToken token) {
        String email = token.getName();
        int adminId = userRepository.findByEmail(email).orElseThrow().getId();
        return questionService.update(id, question, adminId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        questionService.delete(id);
    }
}