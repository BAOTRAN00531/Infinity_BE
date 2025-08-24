// AnswerResponseDto.java
package com.example.infinityweb_be.domain.dto.question.admin;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerResponseDto {
    Integer id;
    String answerText;
    boolean caseSensitive;
    Integer position;
}