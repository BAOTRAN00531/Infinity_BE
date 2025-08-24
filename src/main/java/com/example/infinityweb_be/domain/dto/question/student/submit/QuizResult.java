package com.example.infinityweb_be.domain.dto.question.student.submit;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "question_id", nullable = false)
    private Integer questionId;

    @Column(name = "selected_option_id")
    private Integer selectedOptionId;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @Column(name = "answered_at", nullable = false)
    private LocalDateTime answeredAt;

    // Các mối quan hệ có thể được thêm vào sau này nếu cần
    // @ManyToOne
    // @JoinColumn(name = "user_id", insertable = false, updatable = false)
    // private User user;

    // @ManyToOne
    // @JoinColumn(name = "question_id", insertable = false, updatable = false)
    // private Question question;
}