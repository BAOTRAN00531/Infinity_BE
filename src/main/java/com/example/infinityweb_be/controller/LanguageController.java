package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.Language;
import com.example.infinityweb_be.service.FileStorageService;
import com.example.infinityweb_be.service.LanguageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
@Slf4j
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @Resource
    private FileStorageService fileStorageService;

    @GetMapping
    public List<Language> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Language> getLanguageById(@PathVariable Integer id) {
        return languageService.getLanguageById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Language> createLanguage(
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            @RequestParam("difficulty") String difficulty,
            @RequestParam("popularity") String popularity,
            @RequestParam("flag") MultipartFile flagFile
    ) {
        try {
            String fileName = fileStorageService.storeFile(flagFile);
            Language language = new Language();
            language.setCode(code);
            language.setName(name);
            language.setDifficulty(difficulty);
            language.setPopularity(popularity);
            language.setFlag("/uploads/" + fileName);

            return ResponseEntity.ok(languageService.createLanguage(language));
        } catch (Exception e) {
            log.error("Failed to create language", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Language> updateLanguage(
            @PathVariable Integer id,
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            @RequestParam("difficulty") String difficulty,
            @RequestParam("popularity") String popularity,
            @RequestParam(value = "flag", required = false) MultipartFile flagFile
    ) {
        try {
            Language language = languageService.getLanguageById(id)
                    .orElseThrow(() -> new RuntimeException("Language not found"));

            language.setCode(code);
            language.setName(name);
            language.setDifficulty(difficulty);
            language.setPopularity(popularity);

            if (flagFile != null && !flagFile.isEmpty()) {
                String fileName = fileStorageService.storeFile(flagFile);
                language.setFlag("/uploads/" + fileName);
            }

            Language updatedLanguage = languageService.updateLanguage(language);
            return ResponseEntity.ok(updatedLanguage);

        } catch (Exception e) {
            log.error("Failed to update language", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Integer id) {
        try {
            languageService.deleteLanguage(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Failed to delete language", e);
            return ResponseEntity.notFound().build();
        }
    }
}
