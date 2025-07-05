package com.example.infinityweb_be.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "LanguageTemplates")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class LanguageTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    private String name;
}
