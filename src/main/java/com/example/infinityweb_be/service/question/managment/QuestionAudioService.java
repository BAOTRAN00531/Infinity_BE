package com.example.infinityweb_be.service.question.managment;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.repository.question.QuestionRepository;
import com.example.infinityweb_be.service.TextToSpeechService;
import com.example.infinityweb_be.storage.AudioStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionAudioService {

    private final QuestionRepository questionRepository;
    private final QuestionPayloadService questionPayloadService;
    private final TextToSpeechService textToSpeechService;   // synthesizeBase64FromMap(...)
    private final AudioStorageService audioStorageService;   // lưu file & trả URL

    @Transactional
    public String generateAudio(Long questionId, Map<String, Object> voiceOverride) {
        Question q = questionRepository.findById(questionId.intValue()).orElseThrow();

        Map<String, Object> payload = questionPayloadService.load(q.getId());
        if (payload == null) payload = new HashMap<>();

        String ttsText = (payload.get("tts_text") instanceof String) ? (String) payload.get("tts_text") : null;
        if (ttsText == null || ttsText.isBlank()) {
            throw new IllegalArgumentException("payload.tts_text is required for generate audio");
        }

        // gộp voice từ payload + body override
        Map<String, Object> voice = new HashMap<>();
        if (payload.get("voice") instanceof Map<?,?> v) voice.putAll((Map<String, Object>) v);
        if (voiceOverride != null) voice.putAll(voiceOverride);

        // 1) gọi TTS để lấy base64 MP3
        String base64 = textToSpeechService.synthesizeBase64FromMap(ttsText, voice);

        // 2) lưu base64 ra file -> URL
        String audioUrl = audioStorageService.saveBase64(base64, "mp3");

        // 3) update Question + payload
        q.setAudioUrl(audioUrl);
        questionRepository.save(q);

        payload.put("audio_url", audioUrl);
        if (!voice.isEmpty()) payload.put("voice", voice);
        questionPayloadService.upsert(q, payload);

        return audioUrl;
    }
}