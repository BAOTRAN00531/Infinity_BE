package com.example.infinityweb_be.domain.dto.question;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionOption;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionCreateDto {
    private Integer questionId;
    @NotBlank(message = "optionText must not be blank")
    private String optionText;

    private boolean correct;

    @NotNull(message = "position is required")
    @Min(value = 1, message = "position must be ≥1")
    private Integer position;

    private String imageUrl;

    public QuestionOption toEntity(Question question) {
        return QuestionOption.builder()
                .question(question)
                .optionText(this.optionText)
                .isCorrect(this.correct)
                .position(this.position)
                .imageUrl(this.imageUrl)
                .build();
    }
}

