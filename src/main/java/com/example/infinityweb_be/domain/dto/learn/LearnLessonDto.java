package com.example.infinityweb_be.domain.dto.learn;

public record LearnLessonDto(
        Integer id,
        String title,
        String description,
        String icon,
        Float progress,
        Boolean isUnlocked
) {
}
