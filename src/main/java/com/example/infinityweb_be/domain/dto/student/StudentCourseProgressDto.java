package com.example.infinityweb_be.domain.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

public record StudentCourseProgressDto(
        Integer courseId,
        String courseName,
        String thumbnail,
        BigDecimal price,
        Long totalModules,
        Long completedModules,
        Double progressPercentage // ← đổi từ int sang Double
) {}