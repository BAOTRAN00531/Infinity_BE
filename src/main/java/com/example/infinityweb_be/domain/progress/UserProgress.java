package com.example.infinityweb_be.domain.progress;

import com.example.infinityweb_be.domain.LearningModule;
import com.example.infinityweb_be.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "User_Progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "entity_type")
    private String entityType; // "course", "module", "lesson"

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "progress_percentage", precision = 5, scale = 2)
    private BigDecimal progressPercentage;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}