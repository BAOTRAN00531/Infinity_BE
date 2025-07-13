package com.example.infinityweb_be.domain.dto.question;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionAnswer;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerCreateDto {

    private Integer questionId;

    @NotBlank(message = "answerText must not be blank")
    private String answerText;

    private boolean caseSensitive;

    @NotNull(message = "position is required")
    @Min(value = 1, message = "position must be ≥1")
    private Integer position;

    public QuestionAnswer toEntity(Question question) {
        return QuestionAnswer.builder()
                .question(question)
                .answerText(this.answerText)
                .isCaseSensitive(this.caseSensitive)
                .position(this.position)
                .build();
    }


}
