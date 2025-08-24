package com.example.infinityweb_be.domain.dto.question.student;

import lombok.Data;

@Data
public class StudentQuizAnswerDto {
    private Integer id;
    private String answerText;
    private Integer position;
    // KHÔNG có trường 'caseSensitive' ở đây
}
