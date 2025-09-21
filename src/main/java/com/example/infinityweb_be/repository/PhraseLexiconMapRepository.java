package com.example.infinityweb_be.repository;


import com.example.infinityweb_be.domain.PhraseLexiconMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhraseLexiconMapRepository extends JpaRepository<PhraseLexiconMap, Long> {
    List<PhraseLexiconMap> findByPhraseIdOrderByOrderAsc(Long phraseId);
}
