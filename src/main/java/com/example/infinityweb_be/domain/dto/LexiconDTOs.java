package com.example.infinityweb_be.domain.dto;

import lombok.Data;
import java.util.List;

public class LexiconDTOs {
    @Data public static class SuggestRequest { private String prefix; private String lang; private String topic; private String level; }
    @Data public static class SuggestItemDto { private String word; private String pos; private String ipa; private String glossVi; private Double popularity; private Double confidence; }
    @Data public static class SuggestResponse { private List<SuggestItemDto> suggestions; private String source;}  // "ai" | "hybrid" | "db"}

    @Data public static class EnrichWordRequest { private String text; private String lang; }
    @Data public static class SenseCandidateDto { private String pos; private String ipa; private String glossVi; private List<String> examples; private List<String> collocations; private Double confidence; }
    @Data public static class EnrichWordResponse { private List<SenseCandidateDto> candidates; }

    @Data public static class AnnotatePhraseRequest { private String text; private String langSrc; private String langDst; }
    @Data public static class TokenMapDto { private int i; private Integer lexiconId; private Integer senseId; private String gloss; private String ipa; private String audioUrl; private List<String> alt; }
    @Data public static class AnnotatePhraseResponse { private List<String> tokens; private List<TokenMapDto> map; }
    public static record GlossItem(
            String word, String pos, String ipa, String glossVi, String audioUrl, Double confidence
    ) {}

    public static record GlossPart(
            int index,                 // token index trong câu
            String token,              // text token
            List<GlossItem> items
    ) {}

    public static record GlossRequest(String text, String lang, Long phraseId) {}

    public static record GlossResponse(
            List<String> head,         // ["what","time","is","it"]
            List<GlossPart> parts,     // gloss từng token
            String source              // "phrase-map" | "db" | "ai" | "mixed"
    ) {}
      
}