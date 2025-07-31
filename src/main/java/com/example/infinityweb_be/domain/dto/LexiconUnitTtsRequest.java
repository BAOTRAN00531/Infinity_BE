package com.example.infinityweb_be.domain.dto;

import lombok.Data;

@Data
public class LexiconUnitTtsRequest {
    private String text;
    private String languageCode;
    private String voiceName;
    private String gender; // MALE, FEMALE, NEUTRAL
    private boolean autoGenerateAudio = true;
} 