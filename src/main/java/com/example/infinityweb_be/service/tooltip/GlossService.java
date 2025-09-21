package com.example.infinityweb_be.service.tooltip;

import com.example.infinityweb_be.domain.PhraseTokenMap;
import com.example.infinityweb_be.domain.dto.GlossDTOs;
import com.example.infinityweb_be.domain.dto.LexiconDTOs;
import com.example.infinityweb_be.repository.PhraseTokenMapRepository;
import com.example.infinityweb_be.service.LexiconSuggestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GlossService {

    private final PhraseTokenMapRepository phraseMapRepo;
    private final LexiconSuggestService ai; // bạn đã có service này (Claude/OpenAI) cho fallback
    private final LexiconSuggestService lexiconSuggestService = null;
    public LexiconDTOs.GlossResponse gloss(String text, String lang, Long phraseId) {
        List<String> tokens = tokenize(text);
        List<LexiconDTOs.GlossPart> parts = new ArrayList<>();
        String source = "db";

        // 1) Nếu có phraseId → đọc map đã gán
        Map<Integer, List<PhraseTokenMap>> bucket = new HashMap<>();
        if (phraseId != null) {
            for (PhraseTokenMap m : phraseMapRepo.findByPhraseIdOrderByTokenStartAsc(phraseId)) {
                for (int i = m.getTokenStart(); i <= m.getTokenEnd(); i++) {
                    bucket.computeIfAbsent(i, k -> new ArrayList<>()).add(m);
                }
            }
        }

        for (int i = 0; i < tokens.size(); i++) {
            String tk = tokens.get(i);
            List<LexiconDTOs.GlossItem> items = new ArrayList<>();

            List<PhraseTokenMap> mapped = bucket.getOrDefault(i, List.of());
            if (!mapped.isEmpty()) {
                // ƯU TIÊN dữ liệu map
                for (PhraseTokenMap m : mapped) {
                    String w = (m.getLexiconUnit() != null) ? m.getLexiconUnit().getText() : tk;
                    String pos = (m.getLexiconUnit() != null && m.getLexiconUnit().getPos()!=null)
                            ? m.getLexiconUnit().getPos() : null;
                    items.add(new LexiconDTOs.GlossItem(
                            w,
                            pos,
                            m.getIpa(),
                            m.getGlossVi(),
                            m.getAudioUrl(),
                            1.0
                    ));
                }
                source = "phrase-map";
            } else {
                // 2) không có trong map → có thể gọi AI gợi ý nhanh 1–3 gloss
                try {
                    var aiRes = ai.quickGloss(tk, lang); // bạn có thể thêm method đơn giản trong LexiconSuggestService
                    items.addAll(aiRes);
                    source = source.equals("db") ? "ai" : "mixed";
                } catch (Exception e) {
                    log.warn("AI gloss fail for token {}: {}", tk, e.getMessage());
                }
            }

            parts.add(new LexiconDTOs.GlossPart(i, tk, items));
        }

        return new LexiconDTOs.GlossResponse(tokens, parts, source);
    }
    public GlossDTOs.PhraseGlossResponse gloss(Long phraseId, String lang) {
        List<PhraseTokenMap> mapped = phraseMapRepo
                .findByPhraseIdOrderByTokenStartAsc(phraseId);

        List<GlossDTOs.TokenGloss> items = new ArrayList<>();

        for (PhraseTokenMap m : mapped) {
            String word = (m.getLexiconUnit() != null) ? m.getLexiconUnit().getText() : null;

            String pos = null;
            if (m.getLexiconUnit() != null) {
                // LexiconUnit hiện đang lưu pos ở field "partOfSpeech" (String) – đã có getter getPos()
                try { pos = m.getLexiconUnit().getPos(); } catch (Exception ignore) { }
            }

            // Ưu tiên gloss/ipa/audio lưu tại map, sau đó tới unit/sense
            String glossVi = m.getGlossVi();
            if (glossVi == null && m.getSense() != null) {
                try { glossVi = m.getSense().getGlossVi(); } catch (Exception ignore) { }
            }
            if (glossVi == null && m.getLexiconUnit() != null) {
                try { glossVi = m.getLexiconUnit().getMeaningEng(); } catch (Exception ignore) { }
            }

            String ipa = (m.getIpa() != null) ? m.getIpa()
                    : (m.getLexiconUnit() != null ? m.getLexiconUnit().getIpa() : null);

            String audioUrl = (m.getAudioUrl() != null) ? m.getAudioUrl()
                    : (m.getLexiconUnit() != null ? m.getLexiconUnit().getAudioUrl() : null);

            items.add(GlossDTOs.TokenGloss.builder()
                    .tokenStart(m.getTokenStart())
                    .tokenEnd(m.getTokenEnd())
                    .word(word)
                    .pos(pos)
                    .ipa(ipa)
                    .glossVi(glossVi)
                    .audioUrl(audioUrl)
                    .confidence(0.95)           // dữ liệu DB → tin cậy cao
                    .source("phrase-map")
                    .build());
        }

        // Nếu không có map, bạn có thể (tùy chọn) fallback gọi AI gợi ý nhanh:
        if (items.isEmpty() && lexiconSuggestService != null) {
            log.debug("No phrase map for phraseId={}, fallback AI disabled by default.", phraseId);
            // gợi ý: có thể thêm 1 API khác để FE gọi AI theo từng token text
        }

        return GlossDTOs.PhraseGlossResponse.builder()
                .phraseId(phraseId)
                .lang(lang)
                .tokens(items)
                .build();
    }
    // tokenizer tối giản; nếu FE đã có token list/indices thì gửi thẳng từ FE để đồng bộ 100%
    private List<String> tokenize(String s) {
        return Arrays.stream(s.trim().split("\\s+"))
                .filter(t -> !t.isBlank())
                .toList();
    }
}