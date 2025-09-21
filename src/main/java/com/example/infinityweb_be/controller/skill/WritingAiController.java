package com.example.infinityweb_be.controller.skill;

import com.example.infinityweb_be.domain.dto.skills.WritingDtos;
import com.example.infinityweb_be.service.skill.WritingAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/writing")
@RequiredArgsConstructor
public class WritingAiController {
  private final WritingAiService service;

  @PostMapping("/grade")
  public ResponseEntity<WritingDtos.GradeResponse> grade(@RequestBody WritingDtos.GradeRequest req) {
    return ResponseEntity.ok(service.grade(req));
  }
}