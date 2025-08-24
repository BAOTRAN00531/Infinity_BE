package com.example.infinityweb_be.domain.dto.question.student;

import lombok.Data;

@Data
public class StudentQuizOptionDto {
    private Integer id; // optionId
    private String optionText;
    private String imageUrl; // Có thể giữ lại
    private Integer position;
    // KHÔNG có trường 'correct' ở đây
}