package com.example.infinityweb_be.domain.dto.question.student.submit;

import lombok.Data;
import java.util.Map;

@Data
public class QuizSubmissionDto {
    private Map<Integer, Integer> answers; // questionId -> optionId
    private Integer lessonId;
}