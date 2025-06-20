package com.example.infinityweb_be.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "Question_Options")
@Data
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "option_text")
    private String optionText;

    @JsonProperty("isCorrect")
    @Column(name = "is_correct")
    private boolean isCorrect;

    private Integer position;

    @Column(name = "image_url")
    private String imageUrl;
}