package com.example.infinityweb_be.controller.skill;

import com.example.infinityweb_be.domain.dto.skills.SpeakingScoreDto;
import com.example.infinityweb_be.service.skill.AiSpeakingAssessmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/api/ai/speaking")
@RequiredArgsConstructor
public class AiSpeakingController {
    
    private final AiSpeakingAssessmentService aiAssessmentService;

    /**
     * AI-based speaking assessment - không phụ thuộc rule-based seeds
     * Có thể assess bất kỳ câu nào
     */
    @PostMapping("/ai-assess")
    public ResponseEntity<SpeakingScoreDto> aiAssess(
            @RequestBody AiAssessRequest request) {
        
        try {
            log.info("AI Assessment request - Target: {}, Transcript: {}", 
                request.getTarget(), request.getTranscript());
            
            SpeakingScoreDto result = aiAssessmentService.assessWithAi(
                request.getTranscript(),
                request.getTarget(),
                request.getLanguage()
            );
            
            log.info("AI Assessment completed - Total Score: {}", result.getScoreTotal());
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("AI assessment failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Test endpoint với câu cụ thể
     */
    @PostMapping("/test-assess")
    public ResponseEntity<SpeakingScoreDto> testAssess(
            @RequestParam String target,
            @RequestParam String transcript,
            @RequestParam(defaultValue = "en") String language) {
        
        try {
            SpeakingScoreDto result = aiAssessmentService.assessWithAi(transcript, target, language);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Test assessment failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Upload file audio và assess - DÀNH CHO POSTMAN TEST (không cần transcript)
     */
    @PostMapping(value = "/upload-assess", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SpeakingScoreDto> uploadAndAssess(
            @RequestPart("audio") MultipartFile audioFile,
            @RequestPart("target") String target,
            @RequestPart(value = "language", required = false) String language) {
        
        try {
            log.info("Audio upload request - Target: {}, File size: {} bytes", target, audioFile.getSize());
            
            // Convert file to base64
            byte[] audioBytes = audioFile.getBytes();
            String audioBase64 = Base64.getEncoder().encodeToString(audioBytes);
            
            // For now, we need transcript - in real implementation, you'd use STT service
            // TODO: Integrate with STT service to get transcript from audio
            String transcript = target; // Use target as transcript for testing
            
            SpeakingScoreDto result = aiAssessmentService.assessWithAi(transcript, target, language);
            
            log.info("Upload assessment completed - Total Score: {}", result.getScoreTotal());
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Upload assessment failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Upload file audio + transcript (cho test nhanh)
     */
    @PostMapping(value = "/upload-assess-with-transcript", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SpeakingScoreDto> uploadWithTranscript(
            @RequestPart("audio") MultipartFile audioFile,
            @RequestPart("target") String target,
            @RequestPart("transcript") String transcript,
            @RequestPart(value = "language", required = false) String language) {
        
        try {
            log.info("Audio + Transcript request - Target: {}, Transcript: {}", target, transcript);
            
            SpeakingScoreDto result = aiAssessmentService.assessWithAi(transcript, target, language);
            
            log.info("Assessment completed - Total Score: {}", result.getScoreTotal());
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Assessment failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Request DTO
    public static class AiAssessRequest {
        private String target;        // Câu mục tiêu
        private String transcript;    // Lời người học đã STT
        private String language;      // Ngôn ngữ (en, vi)

        // Getters and Setters
        public String getTarget() { return target; }
        public void setTarget(String target) { this.target = target; }
        
        public String getTranscript() { return transcript; }
        public void setTranscript(String transcript) { this.transcript = transcript; }
        
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
    }
}
