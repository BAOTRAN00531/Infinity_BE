package com.example.infinityweb_be.controller.special;

import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.dto.question.special.SpeakingQuestionDto;
import com.example.infinityweb_be.service.question.special.SpeakingQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions/speaking")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SpeakingQuestionController {
    
    private final SpeakingQuestionService speakingQuestionService;
    private final AuthHelper authHelper;
    
    @GetMapping
    public List<SpeakingQuestionDto> getAll() {
        return speakingQuestionService.getAll();
    }
    
    @GetMapping("/{id}")
    public SpeakingQuestionDto getById(@PathVariable Integer id) {
        return speakingQuestionService.getById(id);
    }
    
    @GetMapping("/lesson/{lessonId}")
    public List<SpeakingQuestionDto> getByLesson(@PathVariable Integer lessonId) {
        return speakingQuestionService.getByLessonId(lessonId);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SpeakingQuestionDto create(@Valid @RequestBody SpeakingQuestionDto dto,
                                     JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return speakingQuestionService.create(dto, adminId);
    }
    
    @PutMapping("/{id}")
    public SpeakingQuestionDto update(@PathVariable Integer id,
                                     @Valid @RequestBody SpeakingQuestionDto dto,
                                     JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return speakingQuestionService.update(id, dto, adminId);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        speakingQuestionService.delete(id);
    }
    
    @PostMapping("/{id}/validate")
    public SpeakingQuestionDto validateSpeaking(@PathVariable Integer id,
                                               @RequestParam String userAudioUrl) {
        return speakingQuestionService.validateSpeaking(id, userAudioUrl);
    }
}
