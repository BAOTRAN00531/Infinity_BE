package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.PhraseTokenMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhraseTokenMapRepository extends JpaRepository<PhraseTokenMap, Long> {
    List<PhraseTokenMap> findByPhraseIdOrderByTokenStartAsc(Long phraseId);
    List<PhraseTokenMap> findByPhraseIdAndTokenStartLessThanEqualAndTokenEndGreaterThanEqual(
            Long phraseId, Integer tokenStart, Integer tokenEnd);
}
