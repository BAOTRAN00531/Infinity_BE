package com.example.infinityweb_be.domain.dto.question.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserModuleQuestionProgressDto {
    private Integer moduleId;
    private Integer questionCount;
    private Integer completedCount;

    public Float getProgress() {
        if (questionCount == null || completedCount == null || questionCount == 0) {
            return 0f;
        }
        return (float) completedCount / (float) questionCount * 100;
    }
}
