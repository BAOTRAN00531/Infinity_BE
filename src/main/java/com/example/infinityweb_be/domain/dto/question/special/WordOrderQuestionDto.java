package com.example.infinityweb_be.domain.dto.question.special;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordOrderQuestionDto {
    
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
    
    @NotEmpty(message = "words list must not be empty")
    @Size(min = 3, message = "words list must have at least 3 words")
    private List<@NotBlank String> words;
    
    @NotEmpty(message = "correctOrder must not be empty")
    @Size(min = 3, message = "correctOrder must have at least 3 positions")
    private List<@NotNull Integer> correctOrder;
    
    private String hint;
    private String explanation;
}
