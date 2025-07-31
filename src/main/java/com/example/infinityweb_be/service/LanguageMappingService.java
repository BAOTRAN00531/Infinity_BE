package com.example.infinityweb_be.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class LanguageMappingService {

    private Map<String, String> languageMappings = new HashMap<>();
    private Map<String, List<String>> supportedVoices = new HashMap<>();
    private String defaultLanguage = "en-US";

    @PostConstruct
    public void init() {
        loadLanguageMappings();
    }

    private void loadLanguageMappings() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("language-mapping.json").getInputStream();
            JsonNode root = mapper.readTree(inputStream);

            // Load language mappings
            JsonNode mappingsNode = root.get("languageMappings");
            if (mappingsNode != null) {
                Iterator<Map.Entry<String, JsonNode>> fields = mappingsNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    languageMappings.put(entry.getKey(), entry.getValue().asText());
                }
            }

            // Load default language
            JsonNode defaultNode = root.get("defaultLanguage");
            if (defaultNode != null) {
                defaultLanguage = defaultNode.asText();
            }

            // Load supported voices
            JsonNode voicesNode = root.get("supportedVoices");
            if (voicesNode != null) {
                Iterator<Map.Entry<String, JsonNode>> fields = voicesNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    List<String> voices = new ArrayList<>();
                    entry.getValue().forEach(voice -> voices.add(voice.asText()));
                    supportedVoices.put(entry.getKey(), voices);
                }
            }

        } catch (IOException e) {
            // Fallback to default mappings if file not found
            System.err.println("Failed to load language mapping file: " + e.getMessage());
            loadDefaultMappings();
        }
    }

    private void loadDefaultMappings() {
        // Fallback mappings for common languages
        languageMappings.put("US", "en-US");
        languageMappings.put("GB", "en-GB");
        languageMappings.put("FR", "fr-FR");
        languageMappings.put("DE", "de-DE");
        languageMappings.put("ES", "es-ES");
        languageMappings.put("IT", "it-IT");
        languageMappings.put("JP", "ja-JP");
        languageMappings.put("KR", "ko-KR");
        languageMappings.put("CN", "zh-CN");
        languageMappings.put("VN", "vi-VN");
        languageMappings.put("RU", "ru-RU");
        languageMappings.put("NL", "nl-NL");
        languageMappings.put("PL", "pl-PL");
        languageMappings.put("TR", "tr-TR");
        languageMappings.put("TH", "th-TH");
        languageMappings.put("IN", "hi-IN");
    }

    public String getTtsLanguageCode(String countryCode) {
        if (countryCode == null || countryCode.trim().isEmpty()) {
            return defaultLanguage;
        }

        // Nếu input đã là language code (có dấu gạch ngang), trả về nguyên
        if (countryCode.contains("-")) {
            return countryCode;
        }

        // Chuyển đổi country code sang language code
        String ttsCode = languageMappings.get(countryCode.toUpperCase());
        return ttsCode != null ? ttsCode : defaultLanguage;
    }

    public Map<String, String> getAllMappings() {
        return new HashMap<>(languageMappings);
    }

    public boolean isSupported(String countryCode) {
        return languageMappings.containsKey(countryCode.toUpperCase());
    }

    public List<String> getSupportedVoices(String languageCode) {
        return supportedVoices.getOrDefault(languageCode, new ArrayList<>());
    }

    public Map<String, List<String>> getAllSupportedVoices() {
        return new HashMap<>(supportedVoices);
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void reloadMappings() {
        loadLanguageMappings();
    }
} 