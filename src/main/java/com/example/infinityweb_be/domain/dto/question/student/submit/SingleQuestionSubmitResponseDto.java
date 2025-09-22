package com.example.infinityweb_be.domain.dto.question.student.submit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SingleQuestionSubmitResponseDto {
    private boolean isCorrect;
    private Integer points;
}
