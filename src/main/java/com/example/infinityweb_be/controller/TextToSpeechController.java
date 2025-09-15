package com.example.infinityweb_be.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/tts")
@RequiredArgsConstructor
@Slf4j
public class TextToSpeechController {

    private static final Pattern REGION = Pattern.compile("^[a-zA-Z]{2}-[A-Z]{2}$");

    private static final Map<String, List<Map<String, String>>> VOICES = Map.of(
            "en-US", List.of(
                    voice("en-US-Standard-A", "FEMALE"),
                    voice("en-US-Standard-B", "MALE"),
                    voice("en-US-Standard-C", "FEMALE")
            ),
            "vi-VN", List.of(
                    voice("vi-VN-Standard-A", "FEMALE"),
                    voice("vi-VN-Standard-B", "MALE")
            ),
            "fr-FR", List.of(
                    voice("fr-FR-Standard-A", "FEMALE"),
                    voice("fr-FR-Standard-B", "MALE")
            )
            // … bạn có thể bổ sung thêm
    );

    private static Map<String, String> voice(String name, String gender) {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("name", name);
        m.put("gender", gender);
        return m;
    }

    /** Chuẩn hóa languageCode: "en" -> "en-US", "vi" -> "vi-VN", giữ nguyên nếu đã có dạng xx-YY. */
    private static String normalizeLang(String code) {
        if (!StringUtils.hasText(code)) return "en-US";
        code = code.trim();
        if (REGION.matcher(code).matches()) return code;
        String base = code.toLowerCase(Locale.ROOT);
        return switch (base) {
            case "en" -> "en-US";
            case "vi" -> "vi-VN";
            case "fr" -> "fr-FR";
            default -> base + "-" + base.toUpperCase(Locale.ROOT);
        };
    }

    @GetMapping("/voices")
    public ResponseEntity<?> listVoices(@RequestParam(value = "languageCode", required = false) String lang) {
        if (!StringUtils.hasText(lang)) {
            // không truyền → trả toàn bộ (gộp tất cả voices)
            List<Map<String, String>> all = new ArrayList<>();
            VOICES.values().forEach(all::addAll);
            return ResponseEntity.ok(all);
        }
        String norm = normalizeLang(lang);
        List<Map<String, String>> list = VOICES.getOrDefault(norm, Collections.emptyList());
        return ResponseEntity.ok(list);
    }

    /** Demo synthesize: trả ra base64 giả lập để FE test nút loa.
     *  Thực tế bạn nối với Google TTS ở service phía dưới đoạn này. */
    @GetMapping(value = "/synthesize", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> synthesize(
            @RequestParam("text") String text,
            @RequestParam("languageCode") String languageCode,
            @RequestParam(value = "voice", required = false) String voice,
            @RequestParam(value = "gender", required = false) String gender
    ) {
        if (!StringUtils.hasText(text)) {
            return ResponseEntity.badRequest().body("Missing text");
        }
        String norm = normalizeLang(languageCode);
        List<Map<String, String>> candidates = VOICES.getOrDefault(norm, Collections.emptyList());

        // chọn voice
        String chosen = (voice != null && candidates.stream().anyMatch(v -> voice.equals(v.get("name"))))
                ? voice
                : candidates.stream()
                .filter(v -> gender == null || gender.equalsIgnoreCase(v.get("gender")))
                .map(v -> v.get("name"))
                .findFirst()
                .orElseGet(() -> candidates.stream().findFirst().map(v -> v.get("name")).orElse(null));

        if (chosen == null) {
            // không có voice phù hợp → trả silent mp3 base64 (hoặc 204)
            return ResponseEntity.ok(""); // để FE rơi vào fallback browser TTS
        }

        // TODO: tích hợp Google Cloud TTS thật ở đây và trả mp3 base64.
        // Demo trả fake base64 (1 byte rỗng) cho chức năng play thử:
        String fakeBase64Mp3 = "/////w==";
        return ResponseEntity.ok(fakeBase64Mp3);
    }
}
