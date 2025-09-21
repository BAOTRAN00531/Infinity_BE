package com.example.infinityweb_be.service.skill;

import com.example.infinityweb_be.domain.dto.skills.WritingDtos;
import com.example.infinityweb_be.service.LlmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WritingAiService {
  private final LlmClient llm;                       // bạn đã có LlmClient
    public WritingDtos.GradeResponse grade(WritingDtos.GradeRequest req) {
      List<String> golds = Optional.ofNullable(req.getAcceptedAnswers()).orElseGet(List::of);
      String user = normalize(req.getUserAnswer());

      // 1) Exact match (nếu có acceptedAnswers)
      boolean exact = !golds.isEmpty() && golds.stream()
              .map(this::normalize).anyMatch(user::equals);

      if (exact) {
        WritingDtos.GradeResponse ok = new WritingDtos.GradeResponse();
        ok.setScore(100); ok.setExactMatch(true);
        ok.setReasons(List.of("Khớp đáp án cho trước."));
        ok.setSuggestions(List.of());
        ok.setAcceptableParaphrases(List.of());
        ok.setNormalizedUser(user);
        return ok;
      }

      // 2) Fallback rule-based nhẹ theo Jaccard nếu có golds
      double rbScore = 0;
      if (!golds.isEmpty()) {
        rbScore = golds.stream().map(this::normalize)
                .mapToDouble(g -> jaccard(user, g)).max().orElse(0) * 100.0;
      }

      // 3) AI grading (nếu có key)
      WritingDtos.GradeResponse out = new WritingDtos.GradeResponse();
      out.setNormalizedUser(user);
      if (llm.isEnabled()) {
        try {
          String schema = """
        {"type":"object","properties":{
          "score":{"type":"number"},
          "semanticallyCorrect":{"type":"boolean"},
          "reasons":{"type":"array","items":{"type":"string"}},
          "suggestions":{"type":"array","items":{"type":"string"}},
          "acceptableParaphrases":{"type":"array","items":{"type":"string"}}
        },"required":["score","semanticallyCorrect","reasons","suggestions"]}""";

          String rubric = Optional.ofNullable(req.getRubric()).orElse(
                  "Judge semantic correctness, completeness, grammar and style for a short answer."
          );

          String prompt = """
        You are a strict but helpful grader for short answers. Focus on whether the answer maintains the MEANING and STRUCTURE of the expected answer.
        Language: %s
        Grading rubric: %s

        User answer:
        ```%s```

        Accepted answers (may be empty):
        %s

        If accepted list is empty, judge based on rubric and general correctness.
        IMPORTANT: Set semanticallyCorrect=true if the answer maintains the core meaning of any accepted answer, even if phrased differently.
        Output JSON only (score 0..100, semanticallyCorrect (boolean), reasons[], suggestions[], acceptableParaphrases[]).
        """.formatted(
                  Optional.ofNullable(req.getLanguage()).orElse("en"),
                  rubric,
                  Optional.ofNullable(req.getUserAnswer()).orElse(""),
                  golds
          );

          Map<String,Object> ai = llm.completeToJsonMap(prompt, schema, 0.2, 256);
          double aiScore = ((Number) ai.getOrDefault("score", rbScore)).doubleValue();
          boolean semanticallyCorrect = Boolean.TRUE.equals(ai.getOrDefault("semanticallyCorrect", false));
          @SuppressWarnings("unchecked")
          List<String> reasons = (List<String>) ai.getOrDefault("reasons", List.of());
          @SuppressWarnings("unchecked")
          List<String> sugg = (List<String>) ai.getOrDefault("suggestions", List.of());
          @SuppressWarnings("unchecked")
          List<String> para = (List<String>) ai.getOrDefault("acceptableParaphrases", List.of());

          // Nếu câu trả lời đúng về mặt ngữ nghĩa, đảm bảo điểm số đủ cao
          double finalScore;
          if (semanticallyCorrect && !golds.isEmpty()) {
            finalScore = Math.max(aiScore, Math.max(rbScore, 85.0)); // Đảm bảo ít nhất 85 điểm nếu đúng ngữ nghĩa
          } else {
            finalScore = golds.isEmpty() ? aiScore : Math.max(aiScore, rbScore);
          }
          
          out.setScore(clamp(finalScore, 0, 100));
          out.setExactMatch(false);
          out.setSemanticallyCorrect(semanticallyCorrect);
          out.setReasons(reasons);
          out.setSuggestions(sugg);
          out.setAcceptableParaphrases(para);
          return out;
        } catch (Exception e) {
          log.warn("LLM writing grade failed, fallback rule-based: {}", e.toString());
        }
      }

      // 4) Chỉ rule-based nếu không có/ lỗi AI
      out.setScore(rbScore);
      out.setExactMatch(false);
      out.setReasons(golds.isEmpty()
              ? List.of("Không có đáp án tham chiếu; không bật AI nên không thể chấm ngữ nghĩa đầy đủ.")
              : List.of("Điểm theo mức độ trùng lặp từ vựng với đáp án tham chiếu."));
      out.setSuggestions(List.of("Viết rõ ràng, sát ý câu hỏi hơn."));
      out.setAcceptableParaphrases(List.of());
      return out;
    }

    // ===== helpers =====
    private String normalize(String s) {
      if (s == null) return "";
      return Normalizer.normalize(s, Normalizer.Form.NFKC)
              .toLowerCase(Locale.ROOT)
              .replaceAll("[^\\p{L}\\p{Nd}\\s']", " ")
              .replaceAll("\\s+", " ")
              .trim();
    }

    private double jaccard(String a, String b) {
      Set<String> A = new HashSet<>(List.of(a.split(" ")));
      Set<String> B = new HashSet<>(List.of(b.split(" ")));
      if (A.isEmpty() && B.isEmpty()) return 1.0;
      Set<String> inter = A.stream().filter(B::contains).collect(Collectors.toSet());
      Set<String> uni = new HashSet<>(A); uni.addAll(B);
      return (double) inter.size() / Math.max(1, uni.size());
    }

    private double clamp(double v, double lo, double hi) { return Math.max(lo, Math.min(hi, v)); }
  }
