package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.LexiconUnit;
import com.example.infinityweb_be.domain.dto.LexiconDTOs;
import com.example.infinityweb_be.repository.LexiconUnitRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LexiconSuggestService {
  private final LlmClient llm;
  private final ObjectMapper om = new ObjectMapper();

  public LexiconDTOs.SuggestResponse suggest(LexiconDTOs.SuggestRequest req) throws Exception {
    String prompt = buildPrompt(req.getPrefix(), req.getLang(), req.getLevel());

    String raw = llm.completeJson(prompt);              // có thể là [] hoặc {items:[...]}
    String normalized = normalizeToArrayJson(raw);      // luôn trả về chuỗi JSON mảng "[]"

    List<Map<String, Object>> arr = om.readValue(
            normalized, new TypeReference<List<Map<String,Object>>>() {});

    List<LexiconDTOs.SuggestItemDto> items = new ArrayList<>();
    for (Map<String,Object> m : arr) {
      LexiconDTOs.SuggestItemDto dto = new LexiconDTOs.SuggestItemDto();
      dto.setWord(s(m.get("word")));
      dto.setPos(s(m.get("pos")));
      dto.setIpa(s(m.get("ipa")));
      dto.setGlossVi(s(m.get("glossVi")));
      dto.setConfidence(d(m.get("confidence")));
      dto.setPopularity(d(m.get("popularity")));
      items.add(dto);
    }

    LexiconDTOs.SuggestResponse res = new LexiconDTOs.SuggestResponse();
    res.setSuggestions(items);
    res.setSource("ai");
    return res;
  }

  private static String s(Object o){ return o==null? null : String.valueOf(o); }
  private static Double d(Object o){
    if (o==null) return null;
    try { return Double.valueOf(String.valueOf(o)); } catch(Exception e){ return null; }
  }

  /** Chuẩn hoá JSON về dạng mảng: nếu là object có "items" thì lấy items */
  private String normalizeToArrayJson(String raw) {
    try {
      JsonNode node = om.readTree(raw.trim());
      if (node.isArray()) return om.writeValueAsString(node);
      if (node.isObject()) {
        if (node.has("items") && node.get("items").isArray()) {
          return om.writeValueAsString(node.get("items"));
        }
        // một số model trả {"suggestions":[...]} – bắt thêm:
        if (node.has("suggestions") && node.get("suggestions").isArray()) {
          return om.writeValueAsString(node.get("suggestions"));
        }
      }
    } catch (Exception ignore) {}
    // fallback: nếu parse thất bại mà chuỗi đã bắt đầu bằng "[" thì giữ nguyên,
    // còn lại trả [] để tránh 500.
    String t = raw.trim();
    return t.startsWith("[") ? t : "[]";
  }

  private String buildPrompt(String prefix, String lang, String level){
    String l = (lang==null||lang.isBlank()) ? "en" : lang;
    String lv = (level==null||level.isBlank()) ? "beginner" : level;
    return """
      You are a lexicon assistant. Return ONLY JSON (no prose).
      Produce Vietnamese gloss (glossVi).

      Return an array of objects with fields:
      - word (string)
      - pos (string)
      - ipa (string)
      - glossVi (short Vietnamese meaning)
      - confidence (0..1)

      Prefix: "%s", Language: "%s", Level: "%s".
      """.formatted(prefix, l, lv);
  }
}