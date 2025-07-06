package com.example.infinityweb_be.domain.dto.modules;

import lombok.Data;

// Dùng cho create/update
@Data
public class LearningModuleRequest {
    private String name;
    private String description;
    private Integer courseId;
    private Integer order;
    private String duration;
    private String status;
}
