package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.dto.LexiconDTOs;
import com.example.infinityweb_be.service.LexiconAnnotateService;
import com.example.infinityweb_be.service.LexiconEnrichService;
import com.example.infinityweb_be.service.LexiconSuggestService;
import com.example.infinityweb_be.service.LlmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ai/lexicon")
@RequiredArgsConstructor
public class LexiconAiController {

  private final LexiconSuggestService suggestService;
  private final LexiconEnrichService enrichService;
  private final LexiconAnnotateService annotateService;
  @Cacheable(cacheNames="lexicon_suggest",
          key="T(org.apache.commons.codec.digest.DigestUtils).sha1Hex(#req.lang+':' + #req.prefix + ':' + #req.level)")

  @PostMapping("/suggest")
  public LexiconDTOs.SuggestResponse suggest(@RequestBody LexiconDTOs.SuggestRequest req) {
    try {
      return suggestService.suggest(req);
    } catch (LlmClient.RateLimitedException e) {
      throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
              e.getMessage() + " retryAfterMs=" + e.retryAfterMs, e);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "LLM failed: " + e.getMessage(), e);
    }
  }

  @PostMapping("/enrich-word")
  public LexiconDTOs.EnrichWordResponse enrich(@RequestBody LexiconDTOs.EnrichWordRequest req) {
    return enrichService.enrich(req);
  }

  @PostMapping("/annotate-phrase")
  public LexiconDTOs.AnnotatePhraseResponse annotate(@RequestBody LexiconDTOs.AnnotatePhraseRequest req) {
    return annotateService.annotate(req);
  }
}