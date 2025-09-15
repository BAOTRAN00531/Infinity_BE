package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.LexiconSense;
import com.example.infinityweb_be.domain.LexiconUnit;
import com.example.infinityweb_be.domain.dto.LexiconDTOs;
import com.example.infinityweb_be.repository.LexiconSenseRepository;
import com.example.infinityweb_be.repository.LexiconUnitRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class LexiconEnrichService {

  public LexiconDTOs.EnrichWordResponse enrich(LexiconDTOs.EnrichWordRequest req) {
    // Mẫu dữ liệu (có thể thay bằng gọi LLM giống suggest nếu muốn)
    var c1 = new LexiconDTOs.SenseCandidateDto();
    c1.setPos("noun"); c1.setIpa("ˈlɛtər"); c1.setGlossVi("lá thư");
    c1.setExamples(List.of("send a letter")); c1.setCollocations(List.of("write ~","post ~"));
    c1.setConfidence(0.92);

    var c2 = new LexiconDTOs.SenseCandidateDto();
    c2.setPos("noun"); c2.setIpa("ˈlɛtər"); c2.setGlossVi("chữ cái");
    c2.setExamples(List.of("capital letters")); c2.setCollocations(List.of("letter of the alphabet"));
    c2.setConfidence(0.88);

    var res = new LexiconDTOs.EnrichWordResponse();
    res.setCandidates(List.of(c1, c2));
    return res;
  }
}
