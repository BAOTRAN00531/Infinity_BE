package com.example.infinityweb_be.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpeakingScoreDto {
private double scoreTotal; // Tổng điểm 0–100
private double pronScore; // Phát âm rõ
private double completeness; // Đủ chữ/đủ nội dung
private double fluency; // Trôi chảy
private String[] tips; // 2–3 gợi ý sửa
}