package com.example.infinityweb_be.domain.dto;

import lombok.*;

import java.util.List;

public class GlossDTOs {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TokenGloss {
        private Integer tokenStart;     // vị trí bắt đầu token trong câu
        private Integer tokenEnd;       // vị trí kết thúc token trong câu
        private String  word;           // từ gốc (ví dụ từ LexiconUnit)
        private String  pos;            // loại từ (String)
        private String  ipa;            // phiên âm
        private String  glossVi;        // nghĩa TV ngắn
        private String  audioUrl;       // link audio nếu có
        private double  confidence;     // 0..1
        private String  source;         // "phrase-map" | "ai" | ...
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PhraseGlossResponse {
        private Long phraseId;
        private String lang;            // ví dụ "en"
        private List<TokenGloss> tokens;
    }
}