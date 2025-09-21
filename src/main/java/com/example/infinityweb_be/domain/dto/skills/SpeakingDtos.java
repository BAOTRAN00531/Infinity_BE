package com.example.infinityweb_be.domain.dto.skills;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class SpeakingDtos {



    @Data
    public static class PhonemeScore {
        private String phoneme;     // /æ/ /ð/ ...
        private double score;       // 0..100
    }

    @Data
    public static class WordResult {
        private String expected;    // từ mong đợi theo targetText
        private String actual;      // từ nghe/nhận được từ ASR (sau normalize)
        private boolean correct;    // khớp hay không (sau align)
        private Double startMs;     // mốc thời gian (nếu ASR trả về)
        private Double endMs;
        private List<PhonemeScore> phonemes; // nếu có từ engine phát âm
        private String feedback;    // gợi ý cho từ này
    }

    @Data
    @Builder
    public static class SpeakingAssessResponse {
        // Fields cho service đơn giản
        private String transcript;          // lời người học
        private double accuracy;            // điểm độ chính xác 0..100
        private double pronunciation;       // điểm phát âm 0..100
        private String feedback;            // feedback từ AI
        
        // Fields cho service phức tạp hơn (có thể để null)
        private Double overallScore;        // 0..100
        private Double correctnessScore;    // đúng từ/ trật tự
        private Double pronunciationScore;  // phát âm (nhấn âm/âm vị)
        private Double fluencyScore;        // độ trôi chảy/ ngắt nghỉ
        private Double completenessScore;   // đọc đủ câu chưa

        private String asrText;             // text sau khi ASR
        private List<WordResult> wordResults;

        private List<String> issues;        // danh sách lỗi (thiếu từ, sai từ, nhấn sai, ... )
        private List<String> suggestions;   // gợi ý cải thiện chung

        private List<String> missingWords;  // từ bị bỏ sót
        private List<String> extraWords;    // từ thừa
        private String stressComment;       // nhận xét nhấn âm tổng quát (nếu có)

        private String modelUsed;           // ví dụ: "google-stt + openai-judge"
        private Long asrTimeMs;             // thời gian chạy ASR
        private Long judgeTimeMs;           // thời gian chấm AI
        private List<String> warnings;      // cảnh báo (âm lượng quá nhỏ, noise, ...)
    }
}