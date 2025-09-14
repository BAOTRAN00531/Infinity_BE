package com.example.infinityweb_be.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptSubmitDto {
    private Long questionId;
    private String skill; // speaking|listening|reading|writing
    private boolean isCorrect;
    private long timeSpentMs;
    private Integer replayCount; // cho bài nghe
    private Map<String, Object> meta; // ví dụ: điểm chi tiết
}