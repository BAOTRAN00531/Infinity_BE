package com.example.infinityweb_be.service.skill;

import com.example.infinityweb_be.domain.dto.skills.SpeakingDtos;
import com.example.infinityweb_be.service.LlmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpeakingAiService {
  private final LlmClient llm;

  /**
   * Service này KHÔNG làm STT/điểm số.
   * Nó chỉ nhận transcript + các điểm số bạn đã tính sẵn (accuracy, pronunciation),
   * sau đó nhờ LLM sinh ra feedback ngắn gọn.
   *
   * @param transcript   lời người học (đã STT)
   * @param targetText   câu mục tiêu cần đọc đúng
   * @param lang         "vi" để feedback tiếng Việt, "en" để feedback tiếng Anh
   * @param accuracy     0..100 (điểm “đúng nội dung” – bạn tính ở SpeakingService)
   * @param pronunciation 0..100 (điểm “phát âm/độ rõ” – từ avgConfidence, v.v.)
   */
  public SpeakingDtos.SpeakingAssessResponse assessFromTranscript(
          String transcript,
          String targetText,
          String lang,
          double accuracy,
          double pronunciation
  ) {

    String schema = """
        {
          "type":"object",
          "properties":{
            "feedback":{"type":"string"}
          },
          "required":["feedback"],
          "additionalProperties": false
        }
        """;

    // Prompt ngắn gọn, trả về JSON-only cho chắc
    String coachLang = (lang == null || lang.isBlank() || lang.startsWith("vi")) ? "Vietnamese" : "English";

    String prompt = """
            You are a concise pronunciation coach. Respond in %s.
            
            Target sentence: "%s"
            Learner transcript: "%s"
            
            Briefly (1-2 sentences) point out:
            - any misread/missing words
            - easy tips to improve stress & clarity
            Return JSON only that matches the given JSON schema.
            """.formatted(coachLang, safe(targetText), safe(transcript));

    String feedback = "Great effort! Keep your pace steady and articulate each word clearly.";
    try {
      Map<String,Object> map =
              llm.completeToJsonMap(prompt, schema, 0.2, 200);
      Object fb = (map == null) ? null : map.get("feedback");
      if (fb != null) feedback = String.valueOf(fb);
    } catch (Exception e) {
      log.warn("LLM feedback failed: {}", e.toString());
    }

    return SpeakingDtos.SpeakingAssessResponse.builder()
            .transcript(transcript)
            .accuracy(clamp01to100(accuracy))
            .pronunciation(clamp01to100(pronunciation))
            .feedback(feedback)
            .build();
  }

  private static double clamp01to100(double v) {
    if (Double.isNaN(v)) return 0;
    return Math.max(0, Math.min(100, v));
  }

  private static String safe(String s) {
    if (s == null) return "";
    return s.replaceAll("[\\u0000-\\u001F]", " ").trim();
  }
}