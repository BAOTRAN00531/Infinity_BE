package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.service.LanguageMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/language-mapping")
@RequiredArgsConstructor
@CrossOrigin
public class LanguageMappingController {

    private final LanguageMappingService languageMappingService;

    @GetMapping("/supported-languages")
    public ResponseEntity<Map<String, String>> getSupportedLanguages() {
        Map<String, String> languages = languageMappingService.getAllMappings();
        return ResponseEntity.ok(languages);
    }

    @GetMapping("/supported-voices")
    public ResponseEntity<Map<String, List<String>>> getSupportedVoices() {
        Map<String, List<String>> voices = languageMappingService.getAllSupportedVoices();
        return ResponseEntity.ok(voices);
    }

    @GetMapping("/voices/{languageCode}")
    public ResponseEntity<List<String>> getVoicesForLanguage(@PathVariable String languageCode) {
        List<String> voices = languageMappingService.getSupportedVoices(languageCode);
        return ResponseEntity.ok(voices);
    }

    @GetMapping("/check-support/{countryCode}")
    public ResponseEntity<Boolean> isLanguageSupported(@PathVariable String countryCode) {
        boolean isSupported = languageMappingService.isSupported(countryCode);
        return ResponseEntity.ok(isSupported);
    }

    @GetMapping("/convert/{countryCode}")
    public ResponseEntity<String> convertToTtsLanguageCode(@PathVariable String countryCode) {
        String ttsCode = languageMappingService.getTtsLanguageCode(countryCode);
        return ResponseEntity.ok(ttsCode);
    }

    @GetMapping("/default-language")
    public ResponseEntity<String> getDefaultLanguage() {
        String defaultLang = languageMappingService.getDefaultLanguage();
        return ResponseEntity.ok(defaultLang);
    }

    @PostMapping("/reload")
    public ResponseEntity<String> reloadMappings() {
        languageMappingService.reloadMappings();
        return ResponseEntity.ok("Language mappings reloaded successfully");
    }
} 