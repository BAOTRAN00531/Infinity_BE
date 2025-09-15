package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.LexiconSense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LexiconSenseRepository extends JpaRepository<LexiconSense, Long> {
    List<LexiconSense> findByLexiconUnitIdOrderByConfidenceDesc(Long unitId);
}
