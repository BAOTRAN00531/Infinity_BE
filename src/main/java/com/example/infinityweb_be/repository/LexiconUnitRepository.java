package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.LexiconUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LexiconUnitRepository extends JpaRepository<LexiconUnit, Integer> {
    List<LexiconUnit> findByLanguage_Code(String code); // ví dụ: "ja"
    List<LexiconUnit> findByType(String type); // ví dụ: "vocabulary", "phrase"
    List<LexiconUnit> findByLanguage_CodeAndType(String languageCode, String type); // ví dụ: "ja", "vocabulary"
}