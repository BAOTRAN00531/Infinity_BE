package com.example.infinityweb_be.controller.skill;

import com.example.infinityweb_be.domain.dto.SpeakingScoreDto;
import com.example.infinityweb_be.service.skill.SpeakingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai/speaking")
@RequiredArgsConstructor
public class SpeakingController {
    private final SpeakingService speakingService;


    @PostMapping(value = "/score", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SpeakingScoreDto> score(
            @RequestPart("audio") MultipartFile audio,
            @RequestPart("text") String text,
            @RequestPart("lang") String lang) {
        return ResponseEntity.ok(speakingService.score(audio, text, lang));
    }
}
