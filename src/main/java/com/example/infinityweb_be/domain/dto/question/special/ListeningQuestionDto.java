package com.example.infinityweb_be.domain.dto.question.special;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListeningQuestionDto {
    
    @NotBlank(message = "questionText must not be blank")
    private String questionText;
    
    @NotNull(message = "lessonId is required")
    @Min(value = 1, message = "lessonId must be ≥1")
    private Integer lessonId;
    
    @NotNull(message = "questionTypeId is required")
    @Min(value = 1, message = "questionTypeId must be ≥1")
    private Integer questionTypeId;
    
    @NotBlank(message = "difficulty must not be blank")
    private String difficulty;
    
    @NotNull(message = "points is required")
    @Min(value = 0, message = "points must be ≥0")
    private Integer points;
    
    @NotBlank(message = "audioUrl must not be blank")
    private String audioUrl;
    
    @NotEmpty(message = "answers list must not be empty")
    @Size(min = 1, message = "answers list must have at least 1 answer")
    private List<@Valid ListeningAnswerDto> answers;
    
    private String hint;
    private String explanation;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListeningAnswerDto {
        @NotBlank(message = "answerText must not be blank")
        private String answerText;
        
        @Builder.Default
        private Boolean caseSensitive = false;
        
        private Integer position;
    }
}
