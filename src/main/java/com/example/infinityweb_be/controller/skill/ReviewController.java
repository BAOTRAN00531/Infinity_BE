package com.example.infinityweb_be.controller.skill;

import com.example.infinityweb_be.service.skill.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/next")
    public ResponseEntity<List<Long>> next(@RequestParam(defaultValue="10") int limit, JwtAuthenticationToken token){
        Long userId = /* lấy từ token */ 1L;
        return ResponseEntity.ok(reviewService.nextQuestions(userId, limit));
    }
}