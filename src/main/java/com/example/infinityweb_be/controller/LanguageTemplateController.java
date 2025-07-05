package com.example.infinityweb_be.controller;


import com.example.infinityweb_be.domain.LanguageTemplate;
import com.example.infinityweb_be.service.LanguageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/language-templates")
public class LanguageTemplateController {
    @Autowired
    private LanguageTemplateService templateService;

    @GetMapping
    public ResponseEntity<List<LanguageTemplate>> getAllTemplates() {
        return ResponseEntity.ok(templateService.getAllTemplates());
    }



}
