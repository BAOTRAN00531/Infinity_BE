package com.example.infinityweb_be.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LexiconUnitUpdateRequest {
    private Integer id;
    private String text;
    private String languageCode; // Thay vì object Language, chỉ cần code
    private String ipa;
    private String audioUrl;
    private String imageUrl;
    private String meaningEng;
    private String partOfSpeech;
    private String type;
    private String difficulty;
    private LocalDateTime createdAt;
} 