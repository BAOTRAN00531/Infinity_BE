package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.LanguageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LanguageTemplateRepository extends JpaRepository<LanguageTemplate, Integer> {

}
