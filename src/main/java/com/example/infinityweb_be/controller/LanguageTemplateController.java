package com.example.infinityweb_be.controller;


import com.example.infinityweb_be.domain.LanguageTemplate;
import com.example.infinityweb_be.domain.dto.LanguageTemplateDTO;
import com.example.infinityweb_be.service.LanguageTemplateService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/language-templates")
public class LanguageTemplateController {
    @Autowired
    private LanguageTemplateService templateService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LanguageTemplateDTO>> getAvailableLanguages() {
        String url = "https://restcountries.com/v3.1/all";
        List<LanguageTemplateDTO> result = new ArrayList<>();

        try {
            WebClient webClient = WebClient.builder()
                .baseUrl("https://restcountries.com")
                .defaultHeader("User-Agent", "Mozilla/5.0")
                .defaultHeader("Accept", "application/json")
                .build();

            String response = webClient.get()
                .uri("/v3.1/all?fields=name,cca2,flags")
                .retrieve()
                .bodyToMono(String.class)
                .block();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            for (JsonNode country : root) {
                String name = country.path("name").path("common").asText();
                String code = country.path("cca2").asText();
                String flag = country.path("flags").path("png").asText();

                if (!name.isEmpty() && !code.isEmpty() && !flag.isEmpty()) {
                    result.add(new LanguageTemplateDTO(name, code, flag));
                }
            }

            result.sort(Comparator.comparing(LanguageTemplateDTO::getName));
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }


}
