package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.LexiconSense;
import com.example.infinityweb_be.domain.LexiconUnit;
import com.example.infinityweb_be.domain.dto.LexiconDTOs;
import com.example.infinityweb_be.repository.LexiconSenseRepository;
import com.example.infinityweb_be.repository.LexiconUnitRepository;
import com.example.infinityweb_be.repository.PhraseTokenMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LexiconAnnotateService {

  private final LexiconUnitRepository unitRepo;

  public LexiconDTOs.AnnotatePhraseResponse annotate(LexiconDTOs.AnnotatePhraseRequest req) {
    String[] toks = req.getText().split("\\s+");
    List<LexiconDTOs.TokenMapDto> maps = new ArrayList<>();

    for (int i = 0; i < toks.length; i++) {
      String tk = toks[i].replaceAll("[^\\p{L}\\p{Nd}\\-']", "");
      if (tk.isEmpty()) continue;

      Optional<LexiconUnit> unitOpt = unitRepo.findOneByTextAndLang(tk, req.getLangSrc());
      if (unitOpt.isEmpty()) continue;
      LexiconUnit unit = unitOpt.get();

      var dto = new LexiconDTOs.TokenMapDto();
      dto.setI(i);
      dto.setLexiconId(unit.getId());          // Integer
      dto.setGloss(unit.getMeaningEng());      // nghĩa TIẾNG VIỆT (đang map vào meaningEng)
      dto.setIpa(unit.getIpa());
      dto.setAudioUrl(unit.getAudioUrl());     // nếu có audio
      maps.add(dto);
    }

    var res = new LexiconDTOs.AnnotatePhraseResponse();
    res.setTokens(Arrays.asList(toks));
    res.setMap(maps);
    return res;
  }
}