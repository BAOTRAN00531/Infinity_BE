package com.example.infinityweb_be.domain.dto.question.special;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Max;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeakingQuestionDto {
    
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
    
    @NotBlank(message = "targetSentence must not be blank")
    private String targetSentence;
    
    @NotBlank(message = "languageCode must not be blank")
    @Pattern(regexp = "^[a-z]{2}-[A-Z]{2}$", message = "languageCode must be in format 'xx-YY'")
    private String languageCode;
    
    private String audioUrl;
    private String pronunciationTips;
    
    @Min(value = 5, message = "timeLimit must be at least 5 seconds")
    @Max(value = 300, message = "timeLimit must not exceed 300 seconds")
    private Integer timeLimit; // in seconds
    
    private String hint;
    private String explanation;
}
