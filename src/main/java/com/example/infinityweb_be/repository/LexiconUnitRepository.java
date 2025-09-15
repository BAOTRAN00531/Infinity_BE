package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.LexiconUnit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LexiconUnitRepository extends JpaRepository<LexiconUnit, Integer> {
    List<LexiconUnit> findByLanguage_Code(String code); // ví dụ: "ja"
    List<LexiconUnit> findByType(String type); // ví dụ: "vocabulary", "phrase"
    List<LexiconUnit> findByLanguage_CodeAndType(String languageCode, String type); // ví dụ: "ja", "vocabulary"
    @Query("""
      SELECT u FROM LexiconUnit u
      JOIN u.language l
      WHERE LOWER(u.text) LIKE LOWER(CONCAT(:prefix, '%'))
        AND (:lang = '' OR LOWER(l.code) = LOWER(:lang))
      ORDER BY u.text ASC
      """)
List<LexiconUnit> findByPrefixAndLang(@Param("prefix") String prefix,
                                     @Param("lang") String lang,
                                     Pageable pageable);

@Query("""
      SELECT u FROM LexiconUnit u
      JOIN u.language l
      WHERE LOWER(u.text) = LOWER(:text)
        AND (:lang = '' OR LOWER(l.code) = LOWER(:lang))
      """)
Optional<LexiconUnit> findOneByTextAndLang(@Param("text") String text,
                                          @Param("lang") String lang);
}