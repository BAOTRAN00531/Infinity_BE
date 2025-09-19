package com.example.infinityweb_be.domain.dto.learn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonProgressDto {
    private Integer lessonId;
    private Integer userId;
    private Boolean isCompleted;
    private Integer progressPercentage;
    private LocalDateTime completedAt;
    private Integer timeSpentMinutes;
    private String notes;
}