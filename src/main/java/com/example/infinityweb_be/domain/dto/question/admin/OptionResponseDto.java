// OptionResponseDto.java
package com.example.infinityweb_be.domain.dto.question.admin;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OptionResponseDto {
    Integer id;
    String optionText;
    boolean correct;
    Integer position;
    String imageUrl;
}