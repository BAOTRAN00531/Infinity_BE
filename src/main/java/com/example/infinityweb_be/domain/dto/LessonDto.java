package com.example.infinityweb_be.domain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO cho Lesson:
 *  - Bỏ qua mọi field lạ (ignoreUnknown)
 *  - Map luôn JSON property "title" vào trường name
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LessonDto {
    private Integer id;

    /** frontend gửi lên key "title", chúng ta map về name */
    @JsonAlias("title")
    private String name;

    private String description;

    @JsonAlias("moduleId")
    private Integer moduleId;

    @JsonAlias("moduleName")
    private String moduleName;

    private String content;
    private String type;

    /** nếu FE gửi lên key "order" thì map vào đây */
    @JsonAlias("order")
    private Integer order;

    private String duration;
    private String status;

    private Integer createdBy;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
}
