// src/main/java/com/example/infinityweb_be/domain/Course.java
package com.example.infinityweb_be.domain.course;

import com.example.infinityweb_be.domain.Language;
import com.example.infinityweb_be.domain.LearningModule;
import com.example.infinityweb_be.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Courses")
// Bỏ qua các field của Hibernate proxy khi serialize
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Course(Integer id) { this.id = id; }

    @Column(nullable = false)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name = "level")
    private String level;

    @Column(name = "duration")
    private String duration;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "thumbnail")
    private String thumbnail;


    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    // tránh serialize đối tượng Language proxy
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnore // bạn thường chỉ cần id/name, không serialize cả User
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    @JsonIgnore
    private User updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<LearningModule> modules = new ArrayList<>();

    @Transient @JsonProperty("modulesCount")
    public int getModulesCount() {
        return (modules == null ? 0 : modules.size());
    }
}
