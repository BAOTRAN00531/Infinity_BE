package com.example.infinityweb_be.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LexiconUnitCreateRequest {
    private String text;
    private String ipa;
    private String audioUrl;
    private String imageUrl;
    private String meaningEng;
    private String partOfSpeech;
    private String type;
    private String difficulty;
} 