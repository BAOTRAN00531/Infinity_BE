package com.example.infinityweb_be.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private Integer id;
    private String name;
    private String description;
    private String content;
    private String type;
    private Integer orderIndex;      // <-- thêm vào đây
    private String duration;
    private String status;
    private Integer moduleId;
    private String moduleName;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
}
