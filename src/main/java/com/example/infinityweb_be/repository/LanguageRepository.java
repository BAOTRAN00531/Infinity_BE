package com.example.infinityweb_be.repository;
import com.example.infinityweb_be.domain.Language;
import com.example.infinityweb_be.domain.dto.LanguageWithCourseCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository

public interface LanguageRepository extends JpaRepository<Language, Integer> {



    @Query("""
        SELECT new com.example.infinityweb_be.domain.dto.LanguageWithCourseCountDTO(
           l.id,
           l.code,
           l.name,
           l.flag,
           l.difficulty,
           l.popularity,
           COUNT(c)
        )
        FROM Language l
        LEFT JOIN Course c ON c.language = l
        GROUP BY
           l.id, l.code, l.name, l.flag, l.difficulty, l.popularity
    """)
    List<LanguageWithCourseCountDTO> findAllWithCourseCount();



}