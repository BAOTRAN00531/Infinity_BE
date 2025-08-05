package com.example.infinityweb_be.domain.dto.student;

import com.example.infinityweb_be.domain.dto.modules.LearningModuleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningCourseDto {
    private Integer courseId;
    private String courseName;
    private String thumbnail;
    private List<LearningModuleDto> modules;
}

