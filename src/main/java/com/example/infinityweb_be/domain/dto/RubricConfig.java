package com.example.infinityweb_be.domain.dto;


import lombok.Data;

import java.util.List;

@Data
public class RubricConfig {
    double jwWeight = 0.4;
    double tokenWeight = 0.6;
    double semWeight = 0.0;          // dùng embeddings (nếu có), mặc định 0
    int minLen = 3;
    int passThreshold = 70;
    List<String> mustContainAny = List.of();
    List<String> mustContainAll = List.of();
    List<String> forbid = List.of();
    List<String> regexMust = List.of();
    List<String> regexForbid = List.of();
    List<String> altTargets = List.of();
    List<PenaltyRule> penalties = List.of();
    @Data public static class PenaltyRule { String pattern; int value; }
}