package com.example.infinityweb_be.controller.skill;

import com.example.infinityweb_be.domain.response.ExplainResponse;
import com.example.infinityweb_be.service.skill.ExplainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class ExplainController {
    private final ExplainService explainService;
    @PostMapping("/explain")
    public ResponseEntity<ExplainResponse> explain(@RequestParam Long questionId){
        return ResponseEntity.ok(new ExplainResponse(explainService.explain(questionId)));
    }
}