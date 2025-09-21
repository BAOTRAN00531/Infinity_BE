package com.example.infinityweb_be.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Lexicon_Units")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LexiconUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    private String ipa;

    private String audioUrl;

    private String imageUrl;

    @Column(name = "meaning_eng")
    private String meaningEng;

    private String partOfSpeech;
    @Column(name = "type")
    private String type; // loại từ (cụm, thành ngữ, từ đơn,...)

    @Column(name = "difficulty")
    private String difficulty; // mức độ dễ/khó (easy, medium, hard)
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public String getPos() {
        return partOfSpeech;
    }
}

