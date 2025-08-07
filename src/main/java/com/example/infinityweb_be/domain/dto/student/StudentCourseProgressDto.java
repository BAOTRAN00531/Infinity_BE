package com.example.infinityweb_be.domain.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseProgressDto {
    private Integer courseId;
    private String courseName;
    private String thumbnail;
    private BigDecimal price;
    private Long totalModules;
    private Long completedModules;
    private Double progressPercentage;
}
