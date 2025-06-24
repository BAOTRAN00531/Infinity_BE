package com.example.infinityweb_be.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningModuleDto {
    private Integer id;
    private String title;
    private String description;
    private Integer courseId;
    private String courseName;
    private Integer order;
    private String duration;
    private String status;
    private long partsCount;

    public LearningModuleDto(Integer id, String title, String description, Integer courseId, String courseName,
                             Integer order, String duration, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.courseId = courseId;
        this.courseName = courseName;
        this.order = order;
        this.duration = duration;
        this.status = status;
    }
}