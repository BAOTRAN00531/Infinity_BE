package com.example.infinityweb_be.domain.dto.question.student.submit;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class QuizResultDto {
    private int totalQuestions;
    private int correctAnswers;
    private double score;
    private Map<Integer, Boolean> results; // questionId -> isCorrect
}