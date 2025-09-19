package com.example.infinityweb_be.domain.dto.learn;

import com.example.infinityweb_be.domain.dto.CourseDto;
import com.example.infinityweb_be.domain.dto.LessonDto;
import com.example.infinityweb_be.domain.dto.modules.LearningModuleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearnPathDto {
    private CourseDto course;
    private List<LearningModuleDto> modules;
    private List<LessonDto> lessons;
    private LessonDto nextLesson;
    private Integer totalLessons;
    private Integer completedLessons;
    private Double progressPercentage;
    private String currentSection;
}