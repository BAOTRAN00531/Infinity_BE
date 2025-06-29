package com.example.infinityweb_be.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Question_Types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * ID-only constructor for reference without full fetch
     */
    public QuestionType(Integer id) {
        this.id = id;
    }

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column(length = 255)
    private String description;

    @Column(name = "min_options", nullable = false)
    private Integer minOptions;

    @Column(name = "min_correct", nullable = false)
    private Integer minCorrect;

    @Column(name = "max_correct")
    private Integer maxCorrect;
}
