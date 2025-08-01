package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.service.TextToSpeechService;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.Voice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tts")
@CrossOrigin
public class TextToSpeechController {

    @Autowired
    private TextToSpeechService textToSpeechService;

    @GetMapping
    public String synthesize(@RequestParam String text) {
        return textToSpeechService.synthesizeText(text);
    }

    @GetMapping("/synthesize")
    public String synthesizeWithLanguage(
            @RequestParam String text,
            @RequestParam(required = false, defaultValue = "en-US") String languageCode) {
        return textToSpeechService.synthesizeText(text, languageCode);
    }

    @GetMapping("/synthesize-with-gender")
    public String synthesizeWithGender(
            @RequestParam String text,
            @RequestParam(required = false, defaultValue = "en-US") String languageCode,
            @RequestParam(required = false, defaultValue = "NEUTRAL") String gender) {
        
        SsmlVoiceGender voiceGender = SsmlVoiceGender.valueOf(gender.toUpperCase());
        return textToSpeechService.synthesizeText(text, languageCode, voiceGender);
    }

    @GetMapping("/synthesize-with-voice")
    public String synthesizeWithVoice(
            @RequestParam String text,
            @RequestParam(required = false, defaultValue = "en-US") String languageCode,
            @RequestParam String voiceName) {
        return textToSpeechService.synthesizeTextWithVoice(text, languageCode, voiceName);
    }

    @GetMapping("/voices")
    public ResponseEntity<List<Voice>> getAvailableVoices(
            @RequestParam(required = false, defaultValue = "en-US") String languageCode) {
        try {
            List<Voice> voices = textToSpeechService.getAvailableVoices(languageCode);
            return ResponseEntity.ok(voices);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/supported-languages")
    public ResponseEntity<Map<String, String>> getSupportedLanguages() {
        Map<String, String> languages = textToSpeechService.getSupportedLanguages();
        return ResponseEntity.ok(languages);
    }

    @GetMapping("/voices/supported")
    public ResponseEntity<Map<String, List<String>>> getSupportedVoices() {
        // Sử dụng LanguageMappingService để lấy danh sách voices được hỗ trợ
        return ResponseEntity.ok(new HashMap<>()); // TODO: Implement this
    }

    @GetMapping("/test-lexicon")
    public String testLexiconTTS(
            @RequestParam String text,
            @RequestParam String languageCode) {
        // Test endpoint để tạo audio cho lexicon unit
        return textToSpeechService.synthesizeText(text, languageCode);
    }
}
