package com.example.infinityweb_be.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class CourseDto {
//    private Integer id;
//    private String name;
//    private String description;
//    private String language;
//    private String level;
//    private String duration;
//    private String status;
//}


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Integer id;
    private String name;
    private String description;
    private String duration;
    private String level;
    private String language;
    private BigDecimal price;
    private String status;
    private String thumbnail;
}

