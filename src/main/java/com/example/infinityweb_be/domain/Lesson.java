package com.example.infinityweb_be.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Lessons")
@Data
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Basic fields
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "content", columnDefinition = "NVARCHAR(MAX)")
    private String content;

    // New metadata fields
    @Column(name = "type", nullable = false)
    private String type;

    /**
     * Không nên dùng tên Java là 'order' vì có thể gây nhầm
     * với thứ tự xử lý; nhưng vẫn map vào cột [order] trong DB.
     */
    @Column(name = "[order]", nullable = false)
    private Integer order;

    @Column(name = "duration")
    private String duration;

    @Column(name = "status", nullable = false)
    private String status;

    // Quan hệ
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "module_id", nullable = false)
    private LearningModule module;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    // Timestamps
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
