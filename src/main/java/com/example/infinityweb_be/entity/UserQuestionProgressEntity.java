package com.example.infinityweb_be.entity;

import com.example.infinityweb_be.domain.LearningModule;
import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_question_progress")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserQuestionProgressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "module_id", nullable = false)
    private LearningModule module;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
