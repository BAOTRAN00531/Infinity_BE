package com.example.infinityweb_be.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    @Column(name = "level")
    private String level;@Column(name = "duration")
    private String duration;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    // ——————————————
    // Thiết lập quan hệ 1−n tới LearningModule
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @JsonIgnore                        // tránh infinite recursion khi JSON
    @EqualsAndHashCode.Exclude        // tránh vòng lặp trong equals/hashCode
    @ToString.Exclude                 // tránh vòng lặp trong toString
    private List<LearningModule> modules = new ArrayList<>();

    /**
     * Jackson sẽ gọi getter này và xuất vào JSON field "modulesCount"
     */
    @JsonProperty("modulesCount")
    @Transient
    public int getModulesCount() {
        return (modules == null ? 0 : modules.size());
    }
}

