package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.Language;
import com.example.infinityweb_be.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {
    @Autowired
    private LanguageRepository languageRepository;

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    public Optional<Language> getLanguageById(Integer id) {
        return languageRepository.findById(id);
    }

    public Language createLanguage(Language language) {
        return languageRepository.save(language);
    }

    public Language updateLanguage(Language updatedLanguage) {
        return languageRepository.save(updatedLanguage); // chỉ cần save nếu đã set đúng dữ liệu ở controller
    }

    public void deleteLanguage(Integer id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found"));
        languageRepository.delete(language);
    }
}
