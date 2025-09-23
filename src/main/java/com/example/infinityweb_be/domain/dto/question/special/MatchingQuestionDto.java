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
public class MatchingQuestionDto {
    
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
    
    @NotBlank(message = "instructions must not be blank")
    private String instructions;
    
    @NotEmpty(message = "pairs list must not be empty")
    @Size(min = 2, message = "pairs list must have at least 2 pairs")
    private List<@Valid MatchingPairDto> pairs;
    
    @Builder.Default
    private Boolean shuffleOptions = false;
    
    private String hint;
    private String explanation;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MatchingPairDto {
        @NotBlank(message = "leftItem must not be blank")
        private String leftItem;
        
        @NotBlank(message = "rightItem must not be blank")
        private String rightItem;
    }
}
