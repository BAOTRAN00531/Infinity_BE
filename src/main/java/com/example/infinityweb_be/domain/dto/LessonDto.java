package com.example.infinityweb_be.domain.dto;

import com.example.infinityweb_be.domain.Lesson; // Make sure to import Lesson
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
    private String videoUrl; // ✅ Thêm trường videoUrl
    private Integer orderIndex;      // <-- thêm vào đây
    private String duration;
    private String status;
    private Integer moduleId;
    private String moduleName;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;

    // ✅ Factory method để chuyển đổi từ Entity sang DTO
    public static LessonDto fromEntity(Lesson lesson) {
        if (lesson == null) {
            return null;
        }

        return new LessonDto(
                lesson.getId(),
                lesson.getName(),
                lesson.getVideoUrl(),
                lesson.getDescription(),
                lesson.getContent(),
                lesson.getType(),
                lesson.getOrderIndex(),
                lesson.getDuration(),
                lesson.getStatus(),
                lesson.getModule() != null ? lesson.getModule().getId() : null,
                lesson.getModule() != null ? lesson.getModule().getName() : null,
                lesson.getCreatedBy() != null ? lesson.getCreatedBy().getId() : null,
                lesson.getCreatedAt(),
                lesson.getUpdatedBy() != null ? lesson.getUpdatedBy().getId() : null,
                lesson.getUpdatedAt()
        );
    }
}