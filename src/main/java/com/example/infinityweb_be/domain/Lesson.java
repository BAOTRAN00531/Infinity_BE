// Lesson.java (Entity mapping for dbo.Lessons) with renamed order -> orderIndex
package com.example.infinityweb_be.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * ID-only constructor for reference without full fetch
     */
    public Lesson(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "module_id", nullable = false)
    private LearningModule module;

    @Column(nullable = false, length = 100)
    private String name;

    private String icon;

    @Column(length = 255)
    private String description;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String content;

    @Column(name = "video_url", length = 255) // ✅ Thêm trường video_url
    private String videoUrl;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    /**
     * Mapped to column [order] in DB, renamed to avoid reserved keyword
     */
    @Column(name = "[order]", nullable = false)
    private Integer orderIndex;

    @Column(length = 50)
    private String duration;

    @Column(nullable = false, length = 20)
    private String status;

    /**
     * ✅ Trường để đánh dấu lesson đã hoàn thành hay chưa
     * Mapped to column is_completed in DB
     */
    @Column(name = "is_completed", nullable = false, columnDefinition = "BIT DEFAULT 0")
    @Builder.Default
    private Boolean isCompleted = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}