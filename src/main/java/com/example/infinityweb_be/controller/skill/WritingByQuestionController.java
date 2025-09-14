package com.example.infinityweb_be.controller.skill;


import com.example.infinityweb_be.domain.response.GradeOpenResponse;
import com.example.infinityweb_be.service.WritingScoringEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class WritingByQuestionController {
    private final WritingScoringEngine engine;

    @PostMapping("/grade-open/by-question")
    public ResponseEntity<GradeOpenResponse> gradeByQuestion(@RequestBody Map<String,Object> body){
        Long qid = ((Number)body.get("questionId")).longValue();
        String text = (String) body.get("text");
        return ResponseEntity.ok(engine.scoreByQuestion(qid, text));
    }
}