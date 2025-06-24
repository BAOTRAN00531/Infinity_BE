package com.example.infinityweb_be.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageWithCourseCountDTO {
    private Integer id;
    private String code;
    private String name;
    private String flag;
    private String difficulty;
    private String popularity;
    private Long courseCount;
}