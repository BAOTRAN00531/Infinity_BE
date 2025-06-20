package com.example.infinityweb_be.domain;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Question_Answers")
@Data
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "answer_text", nullable = false)
    private String answerText;

    private Integer position; // dùng cho sắp xếp từ

    @Column(name = "is_case_sensitive")
    private boolean isCaseSensitive;
}