package com.example.infinityweb_be.domain.dto.question.student.submit;

import lombok.Data;

import java.util.List;

@Data
public class SingleQuestionSubmitDto {
    private Integer questionId;
    private List<Integer> questionOptionIds;
}
