package com.example.infinityweb_be.controller.tooltip;

import com.example.infinityweb_be.domain.dto.GlossDTOs;
import com.example.infinityweb_be.domain.dto.LexiconDTOs;
import com.example.infinityweb_be.service.LexiconSuggestService;
import com.example.infinityweb_be.service.tooltip.GlossService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tooltip")
public class GlossController {

    private final LexiconSuggestService suggestService;
    private final GlossService glossService;

    @GetMapping("/gloss")
    public ResponseEntity<List<LexiconDTOs.GlossItem>> gloss(
            @RequestParam String text,
            @RequestParam String lang,
            @RequestParam(required = false) Long phraseId // có thể null
    ) {
        // nếu cần dùng phraseId thì truyền vào service; ở đây demo dùng quickGloss
        var items = suggestService.quickGloss(text, lang);
        return ResponseEntity.ok(items);
    }
    @GetMapping("/phrase/{phraseId}")
    public ResponseEntity<GlossDTOs.PhraseGlossResponse> getPhraseGloss(
            @PathVariable Long phraseId,
            @RequestParam(defaultValue = "en") String lang
    ) {
        return ResponseEntity.ok(glossService.gloss(phraseId, lang));
    }
}