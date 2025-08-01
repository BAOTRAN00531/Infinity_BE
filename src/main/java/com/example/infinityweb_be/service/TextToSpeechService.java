package com.example.infinityweb_be.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class TextToSpeechService {

    private final LanguageMappingService languageMappingService;

    public TextToSpeechService(LanguageMappingService languageMappingService) {
        this.languageMappingService = languageMappingService;
    }

    public String synthesizeText(String text) {
        return synthesizeText(text, "en-US", SsmlVoiceGender.NEUTRAL);
    }

    public String synthesizeText(String text, String languageCode) {
        return synthesizeText(text, languageCode, SsmlVoiceGender.NEUTRAL);
    }

    public String synthesizeText(String text, String languageCode, SsmlVoiceGender gender) {
        try {
            // Chuyển đổi country code sang language code nếu cần
            String ttsLanguageCode = languageMappingService.getTtsLanguageCode(languageCode);
            
            InputStream credentialsStream = new ClassPathResource("tts_service_main.json").getInputStream();
            TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                    .setCredentialsProvider(() -> GoogleCredentials.fromStream(credentialsStream))
                    .build();

            try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
                SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
                VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                        .setLanguageCode(ttsLanguageCode)
                        .setSsmlGender(gender)
                        .build();
                AudioConfig audioConfig = AudioConfig.newBuilder()
                        .setAudioEncoding(AudioEncoding.MP3)
                        .build();

                SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
                ByteString audioContents = response.getAudioContent();

                return Base64.getEncoder().encodeToString(audioContents.toByteArray());
            }
        } catch (Exception e) {
            throw new RuntimeException("TTS failed: " + e.getMessage(), e);
        }
    }

    public List<Voice> getAvailableVoices(String languageCode) {
        try {
            String ttsLanguageCode = languageMappingService.getTtsLanguageCode(languageCode);
            
            InputStream credentialsStream = new ClassPathResource("tts_service_main.json").getInputStream();
            TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                    .setCredentialsProvider(() -> GoogleCredentials.fromStream(credentialsStream))
                    .build();

            try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
                ListVoicesRequest request = ListVoicesRequest.newBuilder()
                        .setLanguageCode(ttsLanguageCode)
                        .build();
                
                ListVoicesResponse response = textToSpeechClient.listVoices(request);
                return response.getVoicesList();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get voices: " + e.getMessage(), e);
        }
    }

    public String synthesizeTextWithVoice(String text, String languageCode, String voiceName) {
        try {
            String ttsLanguageCode = languageMappingService.getTtsLanguageCode(languageCode);
            
            InputStream credentialsStream = new ClassPathResource("tts_service_main.json").getInputStream();
            TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                    .setCredentialsProvider(() -> GoogleCredentials.fromStream(credentialsStream))
                    .build();

            try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
                SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
                VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                        .setLanguageCode(ttsLanguageCode)
                        .setName(voiceName)
                        .build();
                AudioConfig audioConfig = AudioConfig.newBuilder()
                        .setAudioEncoding(AudioEncoding.MP3)
                        .build();

                SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
                ByteString audioContents = response.getAudioContent();

                return Base64.getEncoder().encodeToString(audioContents.toByteArray());
            }
        } catch (Exception e) {
            throw new RuntimeException("TTS failed: " + e.getMessage(), e);
        }
    }

    public Map<String, String> getSupportedLanguages() {
        return languageMappingService.getAllMappings();
    }
}
