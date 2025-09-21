package com.example.infinityweb_be.service.skill;

import com.example.infinityweb_be.domain.dto.skills.SpeakingScoreDto;
import com.example.infinityweb_be.service.LlmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiSpeakingAssessmentService {
    private final LlmClient llm;

    /**
     * AI-based speaking assessment cho bất kỳ câu nào
     * Không phụ thuộc vào rule-based seeds
     */
    public SpeakingScoreDto assessWithAi(String transcript, String targetSentence, String language) {
        if (!llm.isEnabled()) {
            log.warn("OpenAI not enabled, returning fallback assessment");
            return createFallbackResponse();
        }

        try {
            String schema = """
                {
                  "type": "object",
                  "properties": {
                    "accuracy": {"type": "number", "minimum": 0, "maximum": 100},
                    "pronunciation": {"type": "number", "minimum": 0, "maximum": 100},
                    "completeness": {"type": "number", "minimum": 0, "maximum": 100},
                    "fluency": {"type": "number", "minimum": 0, "maximum": 100},
                    "feedback": {"type": "string"},
                    "detailedIssues": {
                      "type": "array",
                      "items": {"type": "string"}
                    },
                    "stressErrors": {
                      "type": "array", 
                      "items": {
                        "type": "object",
                        "properties": {
                          "word": {"type": "string"},
                          "expectedStress": {"type": "string"},
                          "actualStress": {"type": "string"}
                        }
                      }
                    }
                  },
                  "required": ["accuracy", "pronunciation", "completeness", "fluency", "feedback"]
                }
                """;

            String coachLang = (language == null || language.isBlank() || language.startsWith("vi")) 
                ? "Vietnamese" : "English";

            String prompt = String.format("""
                You are an expert pronunciation coach and language assessor. Analyze the learner's speech and provide detailed assessment.
                
                Language: %s
                Target sentence: "%s"
                Learner transcript: "%s"
                
                Please assess:
                1. ACCURACY (0-100): How correct is the content? Are words pronounced correctly?
                2. PRONUNCIATION (0-100): How clear and accurate are the sounds and stress patterns?
                3. COMPLETENESS (0-100): Did the learner say all the required words?
                4. FLUENCY (0-100): How smooth and natural was the delivery?
                
                Provide specific feedback including:
                - Which words were mispronounced or missing
                - Stress pattern errors (which syllable should be stressed)
                - Specific pronunciation tips
                - Areas for improvement
                
                Be encouraging but specific. Focus on actionable advice.
                
                Return JSON only matching the provided schema.
                """, coachLang, targetSentence, transcript);

            Map<String, Object> result = llm.completeToJsonMap(prompt, schema, 0.3, 500);
            
            return SpeakingScoreDto.builder()
                .scoreTotal(calculateTotalScore(result))
                .pronScore(getDoubleValue(result, "pronunciation"))
                .completeness(getDoubleValue(result, "completeness"))
                .fluency(getDoubleValue(result, "fluency"))
                .tips(buildTipsArray(result))
                .build();

        } catch (Exception e) {
            log.error("AI speaking assessment failed: {}", e.getMessage(), e);
            return createFallbackResponse();
        }
    }

    private double calculateTotalScore(Map<String, Object> result) {
        double accuracy = getDoubleValue(result, "accuracy");
        double pronunciation = getDoubleValue(result, "pronunciation");
        double completeness = getDoubleValue(result, "completeness");
        double fluency = getDoubleValue(result, "fluency");
        
        // Weighted average: accuracy and pronunciation are most important
        return Math.round((accuracy * 0.3 + pronunciation * 0.3 + completeness * 0.25 + fluency * 0.15) * 10.0) / 10.0;
    }

    private double getDoubleValue(Map<String, Object> result, String key) {
        Object value = result.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }

    private String[] buildTipsArray(Map<String, Object> result) {
        StringBuilder tips = new StringBuilder();
        
        String feedback = (String) result.getOrDefault("feedback", "");
        if (!feedback.isEmpty()) {
            tips.append(feedback);
        }

        // Add detailed issues if available
        Object detailedIssues = result.get("detailedIssues");
        if (detailedIssues instanceof java.util.List) {
            @SuppressWarnings("unchecked")
            java.util.List<String> issues = (java.util.List<String>) detailedIssues;
            for (String issue : issues) {
                if (!tips.isEmpty()) tips.append(" ");
                tips.append(issue);
            }
        }

        // Add stress errors if available
        Object stressErrors = result.get("stressErrors");
        if (stressErrors instanceof java.util.List) {
            @SuppressWarnings("unchecked")
            java.util.List<Map<String, Object>> errors = (java.util.List<Map<String, Object>>) stressErrors;
            for (Map<String, Object> error : errors) {
                String word = (String) error.get("word");
                String expected = (String) error.get("expectedStress");
                String actual = (String) error.get("actualStress");
                
                if (word != null && expected != null && actual != null) {
                    if (!tips.isEmpty()) tips.append(" ");
                    tips.append(String.format("Stress error in '%s': should stress '%s', not '%s'.", word, expected, actual));
                }
            }
        }

        // Fallback if no tips generated
        if (tips.isEmpty()) {
            return new String[]{"Good effort! Keep practicing pronunciation and stress patterns."};
        }

        // Split into array, limiting to reasonable length
        String fullTips = tips.toString();
        if (fullTips.length() > 500) {
            fullTips = fullTips.substring(0, 497) + "...";
        }
        
        return new String[]{fullTips};
    }

    private SpeakingScoreDto createFallbackResponse() {
        return SpeakingScoreDto.builder()
            .scoreTotal(70.0)
            .pronScore(70.0)
            .completeness(70.0)
            .fluency(70.0)
            .tips(new String[]{"AI assessment temporarily unavailable. Good effort! Focus on clear pronunciation."})
            .build();
    }
}
