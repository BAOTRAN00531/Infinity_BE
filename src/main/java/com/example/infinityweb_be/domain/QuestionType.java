package com.example.infinityweb_be.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Question_Types")
@Data
public class QuestionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;         // Ví dụ: "multiple_choice_single"
    private String description;  // Mô tả cho admin (VD: "Chọn 1 trong các đáp án")

    @Column(name = "min_options")
    private Integer minOptions;

    @Column(name = "min_correct")
    private Integer minCorrect;

    @Column(name = "max_correct")
    private Integer maxCorrect;
}