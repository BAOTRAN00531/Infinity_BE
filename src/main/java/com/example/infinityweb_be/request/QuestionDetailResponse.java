package com.example.infinityweb_be.request;

import com.example.infinityweb_be.domain.dto.question.managment.QuestionOptionDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QuestionDetailResponse {
    private Integer id;
    private Integer lessonId;
    private Integer questionTypeId;
    private String questionText;
    private String mediaUrl;
    private String audioUrl;
    private String videoUrl;
    private String difficulty;
    private Integer points;

    private Map<String, Object> payload;
    private List<QuestionOptionDto> options;
}
