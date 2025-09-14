package com.example.infinityweb_be.controller.skill;

import com.example.infinityweb_be.domain.dto.AttemptSubmitDto;
import com.example.infinityweb_be.service.skill.AttemptsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptsController {
    private final AttemptsService attemptsService;
    @PostMapping("/submit")
    public ResponseEntity<Void> submit(@RequestBody AttemptSubmitDto dto, JwtAuthenticationToken token){
        Long userId = /* lấy từ token */ 1L;
        attemptsService.recordAttempt(userId, dto);
        return ResponseEntity.ok().build();
    }
}
