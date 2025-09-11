// 6. QuestionCreateDto.java
package com.example.infinityweb_be.domain.dto.question.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionCreateDto {

    @NotBlank(message = "questionText must not be blank")
    private String questionText;

    private Integer courseId;

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

    @Valid
    private MediaDto media;

    @Valid
    private List<@Valid OptionCreateDto> options;

    @Valid
    private List<@Valid AnswerCreateDto> answers;
}