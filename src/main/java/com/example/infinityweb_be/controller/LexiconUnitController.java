package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.LexiconUnit;
import com.example.infinityweb_be.domain.dto.LexiconUnitCreateRequest;
import com.example.infinityweb_be.domain.dto.LexiconUnitResponseDto;
import com.example.infinityweb_be.domain.dto.LexiconUnitUpdateRequest;
import com.example.infinityweb_be.domain.dto.Meta;
import com.example.infinityweb_be.domain.dto.ResultPaginationDTO;
import com.example.infinityweb_be.service.LexiconUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lexicon")
@RequiredArgsConstructor
public class LexiconUnitController {
    private final LexiconUnitService service;

    @GetMapping
    public List<LexiconUnitResponseDto> getAll() {
        return service.getAll().stream()
                .map(LexiconUnitResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/units")
    public ResponseEntity<ResultPaginationDTO> getVocabularyUnits() {
        List<LexiconUnitResponseDto> data = service.findByType("vocabulary").stream()
                .map(LexiconUnitResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        ResultPaginationDTO response = new ResultPaginationDTO();
        response.setResult(data);
        
        Meta meta = new Meta();
        meta.setPage(1);
        meta.setPageSize(data.size());
        meta.setPages(1);
        meta.setTotal(data.size());
        response.setMeta(meta);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/phrases")
    public ResponseEntity<ResultPaginationDTO> getPhrases() {
        List<LexiconUnitResponseDto> data = service.findByType("phrase").stream()
                .map(LexiconUnitResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        ResultPaginationDTO response = new ResultPaginationDTO();
        response.setResult(data);
        
        Meta meta = new Meta();
        meta.setPage(1);
        meta.setPageSize(data.size());
        meta.setPages(1);
        meta.setTotal(data.size());
        response.setMeta(meta);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/language/{code}")
    public List<LexiconUnitResponseDto> getByLanguage(@PathVariable String code) {
        return service.findByLanguage(code).stream()
                .map(LexiconUnitResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/language/{code}/units")
    public List<LexiconUnitResponseDto> getVocabularyByLanguage(@PathVariable String code) {
        return service.findByLanguageAndType(code, "vocabulary").stream()
                .map(LexiconUnitResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/language/{code}/phrases")
    public List<LexiconUnitResponseDto> getPhrasesByLanguage(@PathVariable String code) {
        return service.findByLanguageAndType(code, "phrase").stream()
                .map(LexiconUnitResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping
    public LexiconUnitResponseDto create(@RequestBody LexiconUnitCreateRequest request,
                               @RequestParam String languageCode) {
        return LexiconUnitResponseDto.fromEntity(service.create(request, languageCode));
    }

    @PutMapping("/{id}")
    public LexiconUnitResponseDto update(@PathVariable Integer id,
                               @RequestBody LexiconUnitUpdateRequest request) {
        return LexiconUnitResponseDto.fromEntity(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/generate-audio")
    public LexiconUnitResponseDto generateAudio(@PathVariable Integer id) {
        return LexiconUnitResponseDto.fromEntity(service.generateAudioForLexicon(id));
    }

    @PostMapping("/{id}/generate-audio-with-voice")
    public LexiconUnitResponseDto generateAudioWithVoice(@PathVariable Integer id,
                                               @RequestParam String voiceName) {
        return LexiconUnitResponseDto.fromEntity(service.generateAudioWithVoice(id, voiceName));
    }

    @GetMapping("/search")
    public List<LexiconUnitResponseDto> search(@RequestParam String q,
                                   @RequestParam(required = false) String languageCode,
                                   @RequestParam(required = false) String type) {
        return service.search(q, languageCode, type).stream()
                .map(LexiconUnitResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/filter")
    public List<LexiconUnitResponseDto> filter(@RequestParam(required = false) String partOfSpeech,
                                   @RequestParam(required = false) String difficulty,
                                   @RequestParam(required = false) String languageCode,
                                   @RequestParam(required = false) String type) {
        return service.filter(partOfSpeech, difficulty, languageCode, type).stream()
                .map(LexiconUnitResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping("/sample-data")
    public ResponseEntity<String> createSampleData() {
        try {
            service.createSampleData();
            return ResponseEntity.ok("Sample data created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create sample data: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> test() {
        try {
            long count = service.getAll().size();
            return ResponseEntity.ok("Database has " + count + " lexicon units");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/test-data")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<LexiconUnitResponseDto>> testData() {
        try {
            List<LexiconUnitResponseDto> data = service.getAll().stream()
                    .limit(3) // Chỉ lấy 3 records đầu tiên
                    .map(LexiconUnitResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
