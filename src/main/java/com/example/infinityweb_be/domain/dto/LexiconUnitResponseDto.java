package com.example.infinityweb_be.domain.dto;

import com.example.infinityweb_be.domain.Language;
import com.example.infinityweb_be.domain.LexiconUnit;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LexiconUnitResponseDto {
    private Integer id;
    private String text;
    private String ipa;
    private String audioUrl;
    private String imageUrl;
    private String meaningEng;
    private String partOfSpeech;
    private String type;
    private String difficulty;
    private LocalDateTime createdAt;
    private LanguageInfo language;

    @Data
    public static class LanguageInfo {
        private Integer id;
        private String code;
        private String name;
        private String flag;
    }

    public static LexiconUnitResponseDto fromEntity(LexiconUnit entity) {
        LexiconUnitResponseDto dto = new LexiconUnitResponseDto();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setIpa(entity.getIpa());
        dto.setAudioUrl(entity.getAudioUrl());
        dto.setImageUrl(entity.getImageUrl());
        dto.setMeaningEng(entity.getMeaningEng());
        dto.setPartOfSpeech(entity.getPartOfSpeech());
        dto.setType(entity.getType());
        dto.setDifficulty(entity.getDifficulty());
        dto.setCreatedAt(entity.getCreatedAt());
        
        if (entity.getLanguage() != null) {
            LanguageInfo languageInfo = new LanguageInfo();
            languageInfo.setId(entity.getLanguage().getId());
            languageInfo.setCode(entity.getLanguage().getCode());
            languageInfo.setName(entity.getLanguage().getName());
            languageInfo.setFlag(entity.getLanguage().getFlag());
            dto.setLanguage(languageInfo);
        }
        
        return dto;
    }
} 