package com.example.infinityweb_be.domain.dto.modules;

import com.example.infinityweb_be.domain.dto.LessonDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningModuleDto {
    private Integer id;

    private String name;
    private String description;

    private Integer courseId;
    private String courseName;

    private Integer order;
    private String duration;
    private String status;

    private long partsCount;



    public LearningModuleDto(Integer id, String name, String description, Integer courseId, String courseName,
                             Integer order, String duration, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courseId = courseId;
        this.courseName = courseName;
        this.order = order;
        this.duration = duration;
        this.status = status;
    }


}
