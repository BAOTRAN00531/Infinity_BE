package com.example.infinityweb_be.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SpeakingAssessResponse {
  private String transcript;   // ASR text
  private double accuracy;     // so với target (0..100)
  private double pronunciation;// xấp xỉ (0..100) - proxy
  private String feedback;     // gợi ý sửa
}