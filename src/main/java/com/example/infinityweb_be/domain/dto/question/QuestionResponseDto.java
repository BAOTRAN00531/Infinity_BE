// QuestionResponseDto.java
package com.example.infinityweb_be.domain.dto.question;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionResponseDto  {
    Integer id;
    String questionText;
    Integer courseId;
    Integer lessonId;
    Integer questionTypeId;
    String difficulty;
    Integer points;
    MediaDto media;
    List<OptionResponseDto> options;
    List<AnswerResponseDto> answers;

    Integer createdBy;
    LocalDateTime createdAt;
    Integer updatedBy;
    LocalDateTime updatedAt;

    private String lessonName;
    private Integer moduleId;
    private String moduleName;
}
