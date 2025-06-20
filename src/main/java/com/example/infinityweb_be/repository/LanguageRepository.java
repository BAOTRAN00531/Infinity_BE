package com.example.infinityweb_be.repository;
import com.example.infinityweb_be.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Integer> {
}