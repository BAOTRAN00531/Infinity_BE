package com.example.infinityweb_be.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class TextToSpeechService {

    private final LanguageMappingService languageMappingService;

    public TextToSpeechService(LanguageMappingService languageMappingService) {
        this.languageMappingService = languageMappingService;
    }

    // ==== tạo client dùng ADC (đọc GOOGLE_APPLICATION_CREDENTIALS) ====
    private TextToSpeechClient newClient() throws Exception {
        String envPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (envPath == null || envPath.isBlank()) {
            throw new IllegalStateException("GOOGLE_APPLICATION_CREDENTIALS not set");
        }

        GoogleCredentials creds;
        try (java.io.FileInputStream in = new java.io.FileInputStream(envPath)) {
            creds = GoogleCredentials.fromStream(in);
        }

        // Gán scope Cloud Platform để có token hợp lệ
        creds = creds.createScoped(java.util.List.of("https://www.googleapis.com/auth/cloud-platform"));

        // Nếu là service account, gắn quota project và log xác nhận
        if (creds instanceof ServiceAccountCredentials sac) {
            System.out.println("TTS Using SA: " + sac.getClientEmail() + " | project: " + sac.getProjectId());
            String quotaProject = sac.getProjectId();
            if (quotaProject != null && !quotaProject.isBlank()) {
                creds = sac.createWithQuotaProject(quotaProject);
            }
        } else {
            System.out.println("TTS Creds class: " + creds.getClass().getName());
        }

        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(creds))
                .build();

        return TextToSpeechClient.create(settings);
    }

    public String synthesizeText(String text) {
        return synthesizeText(text, "en-US", SsmlVoiceGender.NEUTRAL);
    }

    public String synthesizeText(String text, String languageCode) {
        return synthesizeText(text, languageCode, SsmlVoiceGender.NEUTRAL);
    }

    public String synthesizeText(String text, String languageCode, SsmlVoiceGender gender) {
        try (TextToSpeechClient client = newClient()) {
            String ttsLanguageCode = languageMappingService.getTtsLanguageCode(languageCode);
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(ttsLanguageCode)
                    .setSsmlGender(gender)
                    .build();
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            SynthesizeSpeechResponse response = client.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioContents = response.getAudioContent();
            return Base64.getEncoder().encodeToString(audioContents.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("TTS failed: " + e.getMessage(), e);
        }
    }

    public List<Voice> getAvailableVoices(String languageCode) {
        try (TextToSpeechClient client = newClient()) {
            String ttsLanguageCode = languageMappingService.getTtsLanguageCode(languageCode);
            ListVoicesRequest request = ListVoicesRequest.newBuilder()
                    .setLanguageCode(ttsLanguageCode)
                    .build();
            return client.listVoices(request).getVoicesList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get voices: " + e.getMessage(), e);
        }
    }

    public String synthesizeTextWithVoice(String text, String languageCode, String voiceName) {
        try (TextToSpeechClient client = newClient()) {
            String ttsLanguageCode = languageMappingService.getTtsLanguageCode(languageCode);
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(ttsLanguageCode)
                    .setName(voiceName)
                    .build();
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            SynthesizeSpeechResponse response = client.synthesizeSpeech(input, voice, audioConfig);
            return Base64.getEncoder().encodeToString(response.getAudioContent().toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("TTS failed: " + e.getMessage(), e);
        }
    }

    public String synthesizeBase64FromMap(String text, Map<String, Object> voiceCfg) {
        try (TextToSpeechClient client = newClient()) {
            if (text == null || text.isBlank()) throw new IllegalArgumentException("text is required");

            String languageCode = getStr(voiceCfg, "languageCode", "en-US");
            String voiceName    = getStr(voiceCfg, "voice", null);           // ví dụ: en-US-Neural2-A
            String encoding     = getStr(voiceCfg, "encoding", "MP3");       // MP3/OGG_OPUS/LINEAR16
            double speed        = getDouble(voiceCfg, "speed", 1.0);
            double pitch        = getDouble(voiceCfg, "pitch", 0.0);

            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            VoiceSelectionParams.Builder vb = VoiceSelectionParams.newBuilder().setLanguageCode(languageCode);
            if (voiceName != null && !voiceName.isBlank()) vb.setName(voiceName);

            AudioEncoding ae = switch (encoding.toUpperCase()) {
                case "OGG_OPUS" -> AudioEncoding.OGG_OPUS;
                case "LINEAR16" -> AudioEncoding.LINEAR16;
                default -> AudioEncoding.MP3;
            };
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(ae)
                    .setSpeakingRate(speed)
                    .setPitch(pitch)
                    .build();

            SynthesizeSpeechResponse res = client.synthesizeSpeech(input, vb.build(), audioConfig);
            return Base64.getEncoder().encodeToString(res.getAudioContent().toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("TTS failed: " + e.getMessage(), e);
        }
    }

    // helpers
    private static String getStr(Map<String, Object> m, String k, String def) {
        if (m == null) return def;
        Object v = m.get(k);
        return v == null ? def : String.valueOf(v);
    }
    private static double getDouble(Map<String, Object> m, String k, double def) {
        if (m == null) return def;
        Object v = m.get(k);
        if (v instanceof Number n) return n.doubleValue();
        try { return v == null ? def : Double.parseDouble(String.valueOf(v)); } catch (Exception ignore) { return def; }
    }
}
