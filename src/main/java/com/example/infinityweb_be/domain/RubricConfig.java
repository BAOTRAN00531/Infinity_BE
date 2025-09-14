package com.example.infinityweb_be.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RubricConfig {

    // Trọng số trộn điểm (0..1), tổng không bắt buộc = 1
    private double jwWeight    = 0.4;  // Jaro–Winkler per-sentence
    private double tokenWeight = 0.6;  // giống nhau theo từng từ
    private double semWeight   = 0.0;  // embeddings (nếu có microservice)

    // Yêu cầu chung
    private int    minLen        = 3;   // tối thiểu số token
    private int    passThreshold = 70;  // ngưỡng “đạt”

    // Ràng buộc theo từ/regex
    private List<String> mustContainAny  = new ArrayList<>();
    private List<String> mustContainAll  = new ArrayList<>();
    private List<String> forbid          = new ArrayList<>();
    private List<String> regexMust       = new ArrayList<>();
    private List<String> regexForbid     = new ArrayList<>();

    // Đáp án thay thế (ngoài correct_text/alt_answers_json trong bảng Questions)
    private List<String> altTargets      = new ArrayList<>();

    // Phạt mềm & phạt cứng (hardPenalties có thể hạ trần điểm)
    private List<PenaltyRule> penalties      = new ArrayList<>();
    private List<PenaltyRule> hardPenalties  = new ArrayList<>();

    @Data
    @NoArgsConstructor
    public static class PenaltyRule {
        private String pattern;  // regex pattern (Java regex)
        private int    value = 0; // số điểm phạt (điểm)
    }
    @Data @NoArgsConstructor
    public static class HardZeroRule {
        private String pattern;   // regex
        private String message;   // feedback khi 0 điểm
    }

    private List<HardZeroRule> hardZeroRules = new ArrayList<>();

}