package com.example.infinityweb_be.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Languages")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String flag;
    @Column(nullable = true)
    private String difficulty;

    @Column(nullable = true)
    private String popularity;

}
