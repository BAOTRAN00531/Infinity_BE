package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.LanguageTemplate;
import com.example.infinityweb_be.repository.LanguageTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LanguageTemplateService {
    @Autowired
    private LanguageTemplateRepository templateRepo;

    public List<LanguageTemplate> getAllTemplates() {
        return templateRepo.findAll();
    }
}