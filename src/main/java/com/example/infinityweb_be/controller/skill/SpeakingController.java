package com.example.infinityweb_be.controller.skill;

import com.example.infinityweb_be.domain.dto.SpeakingScoreDto;
import com.example.infinityweb_be.service.skill.SpeakingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai/speaking")
@RequiredArgsConstructor
public class SpeakingController {
    private final SpeakingService speakingService;

    // 1) Gửi file audio (multipart): audio + (target | questionId) + lang
    @PostMapping(value = "/score", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SpeakingScoreDto> scoreMultipart(
            @RequestPart("audio") MultipartFile audio,
            @RequestPart(value = "target", required = false) String target,
            @RequestPart(value = "questionId", required = false) Long questionId,
            @RequestPart(value = "lang", required = false) String lang
    ) throws Exception {
        var dto = speakingService.grade(audio, null, lang, target, questionId);
        return ResponseEntity.ok(dto);
    }

    // 2) Gửi base64 (JSON): audioBase64 + (target | questionId) + lang
    @PostMapping(value = "/score-base64", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpeakingScoreDto> scoreBase64(@RequestBody ScoreBase64Req req) throws Exception {
        var dto = speakingService.grade(null, req.audioBase64(), req.lang(), req.target(), req.questionId());
        return ResponseEntity.ok(dto);
    }

    // DTO req đơn giản cho JSON
    private record ScoreBase64Req(String audioBase64, String target, Long questionId, String lang) {}
}
