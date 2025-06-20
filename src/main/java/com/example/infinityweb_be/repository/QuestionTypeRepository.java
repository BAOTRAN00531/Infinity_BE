package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionTypeRepository extends JpaRepository<QuestionType, Integer> {
}