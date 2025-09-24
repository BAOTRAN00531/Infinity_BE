package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.Language;
import com.example.infinityweb_be.domain.LexiconUnit;
import com.example.infinityweb_be.domain.dto.LexiconUnitCreateRequest;
import com.example.infinityweb_be.domain.dto.LexiconUnitUpdateRequest;
import com.example.infinityweb_be.repository.LanguageRepository;
import com.example.infinityweb_be.repository.LexiconUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LexiconUnitService {

    private final LexiconUnitRepository repository;
    private final LanguageRepository languageRepository;
    private final TextToSpeechService textToSpeechService;
    private final AudioFileService audioFileService;

    public List<LexiconUnit> getAll() {
        return repository.findAll();
    }

    public List<LexiconUnit> findByType(String type) {
        return repository.findByType(type);
    }

    public List<LexiconUnit> findByLanguageAndType(String languageCode, String type) {
        return repository.findByLanguage_CodeAndType(languageCode, type);
    }

    public List<LexiconUnit> search(String query, String languageCode, String type) {
        List<LexiconUnit> results = repository.findAll();
        
        return results.stream()
                .filter(unit -> {
                    boolean matchesQuery = unit.getText().toLowerCase().contains(query.toLowerCase()) ||
                            (unit.getIpa() != null && unit.getIpa().toLowerCase().contains(query.toLowerCase())) ||
                            (unit.getMeaningEng() != null && unit.getMeaningEng().toLowerCase().contains(query.toLowerCase()));
                    
                    boolean matchesLanguage = languageCode == null || 
                            unit.getLanguage().getCode().equalsIgnoreCase(languageCode);
                    
                    boolean matchesType = type == null || 
                            (unit.getType() != null && unit.getType().equalsIgnoreCase(type));
                    
                    return matchesQuery && matchesLanguage && matchesType;
                })
                .collect(Collectors.toList());
    }

    public List<LexiconUnit> filter(String partOfSpeech, String difficulty, String languageCode, String type) {
        // Implementation for filtering lexicon units
        List<LexiconUnit> allUnits = repository.findAll();
        
        return allUnits.stream()
                .filter(unit -> partOfSpeech == null || partOfSpeech.equals(unit.getPartOfSpeech()))
                .filter(unit -> difficulty == null || difficulty.equals(unit.getDifficulty()))
                .filter(unit -> languageCode == null || languageCode.equals(unit.getLanguage().getCode()))
                .filter(unit -> type == null || type.equals(unit.getType()))
                .collect(Collectors.toList());
    }

    public void createSampleData() {
        // Check if sample data already exists
        if (!repository.findAll().isEmpty()) {
            System.out.println("Sample data already exists, skipping...");
            return; // Sample data already exists
        }

        System.out.println("Creating sample data...");

        // Get or create languages - using codes from infinityv2.sql
        Language enLanguage = languageRepository.findByCode("en").orElse(null);
        Language jaLanguage = languageRepository.findByCode("ja").orElse(null);
        Language viLanguage = languageRepository.findByCode("vi").orElse(null);

        System.out.println("Found languages: en=" + (enLanguage != null) + ", ja=" + (jaLanguage != null) + ", vi=" + (viLanguage != null));

        if (enLanguage == null) {
            System.out.println("Creating English language...");
            enLanguage = new Language();
            enLanguage.setCode("en");
            enLanguage.setName("English");
            enLanguage.setFlag("🇺🇸");
            enLanguage = languageRepository.save(enLanguage);
        }

        if (jaLanguage == null) {
            System.out.println("Creating Japanese language...");
            jaLanguage = new Language();
            jaLanguage.setCode("ja");
            jaLanguage.setName("Japanese");
            jaLanguage.setFlag("🇯🇵");
            jaLanguage = languageRepository.save(jaLanguage);
        }

        if (viLanguage == null) {
            System.out.println("Creating Vietnamese language...");
            viLanguage = new Language();
            viLanguage.setCode("vi");
            viLanguage.setName("Tiếng Việt");
            viLanguage.setFlag("🇻🇳");
            viLanguage = languageRepository.save(viLanguage);
        }

        // Create sample vocabulary
        System.out.println("Creating sample vocabulary...");
        
        LexiconUnit hello = LexiconUnit.builder()
                .text("Hello")
                .language(enLanguage)
                .ipa("/həˈloʊ/")
                .meaningEng("A greeting")
                .partOfSpeech("noun")
                .type("vocabulary")
                .difficulty("beginner")
                .build();
        repository.save(hello);

        LexiconUnit goodbye = LexiconUnit.builder()
                .text("Goodbye")
                .language(enLanguage)
                .ipa("/ˌɡʊdˈbaɪ/")
                .meaningEng("A farewell")
                .partOfSpeech("noun")
                .type("vocabulary")
                .difficulty("beginner")
                .build();
        repository.save(goodbye);

        LexiconUnit konnichiwa = LexiconUnit.builder()
                .text("こんにちは")
                .language(jaLanguage)
                .ipa("/konnichiwa/")
                .meaningEng("Hello/Good afternoon")
                .partOfSpeech("noun")
                .type("vocabulary")
                .difficulty("beginner")
                .build();
        repository.save(konnichiwa);

        LexiconUnit xinChao = LexiconUnit.builder()
                .text("Xin chào")
                .language(viLanguage)
                .ipa("/sin tʃaʊ/")
                .meaningEng("Hello")
                .partOfSpeech("phrase")
                .type("vocabulary")
                .difficulty("beginner")
                .build();
        repository.save(xinChao);

        // Create sample phrases
        System.out.println("Creating sample phrases...");
        
        LexiconUnit howAreYou = LexiconUnit.builder()
                .text("How are you?")
                .language(enLanguage)
                .ipa("/haʊ ɑːr juː/")
                .meaningEng("A question asking about someone's well-being")
                .partOfSpeech("phrase")
                .type("phrase")
                .difficulty("beginner")
                .build();
        repository.save(howAreYou);

        LexiconUnit ogenkiDesuKa = LexiconUnit.builder()
                .text("お元気ですか")
                .language(jaLanguage)
                .ipa("/ogenki desu ka/")
                .meaningEng("How are you? (polite)")
                .partOfSpeech("phrase")
                .type("phrase")
                .difficulty("intermediate")
                .build();
        repository.save(ogenkiDesuKa);

        LexiconUnit banKhoeKhong = LexiconUnit.builder()
                .text("Bạn khỏe không?")
                .language(viLanguage)
                .ipa("/ban kwe kʰoŋ/")
                .meaningEng("How are you?")
                .partOfSpeech("phrase")
                .type("phrase")
                .difficulty("beginner")
                .build();
        repository.save(banKhoeKhong);

        System.out.println("Sample data created successfully! Total lexicon units: " + repository.findAll().size());
    }

    public LexiconUnit create(LexiconUnit unit, String languageCode) {
        Language lang = languageRepository.findByCode(languageCode)
                .orElseThrow(() -> new RuntimeException("Language not found"));

        unit.setLanguage(lang);
        unit.setCreatedAt(LocalDateTime.now());
        
        // Tự động tạo audio nếu chưa có
        if (unit.getAudioUrl() == null || unit.getAudioUrl().isEmpty()) {
            try {
                String audioBase64 = textToSpeechService.synthesizeText(unit.getText(), languageCode);
                String audioUrl = audioFileService.saveAudioFromBase64(audioBase64, unit.getText(), languageCode);
                unit.setAudioUrl(audioUrl);
            } catch (Exception e) {
                // Log error nhưng không fail việc tạo lexicon unit
                System.err.println("Failed to generate audio for lexicon unit: " + e.getMessage());
            }
        }
        
        return repository.save(unit);
    }

    public LexiconUnit create(LexiconUnitCreateRequest request, String languageCode) {
        Language lang = languageRepository.findByCode(languageCode)
                .orElseThrow(() -> new RuntimeException("Language not found with code: " + languageCode));

        LexiconUnit unit = new LexiconUnit();
        unit.setText(request.getText());
        unit.setIpa(request.getIpa());
        unit.setImageUrl(request.getImageUrl());
        unit.setAudioUrl(request.getAudioUrl());
        unit.setMeaningEng(request.getMeaningEng());
        unit.setPartOfSpeech(request.getPartOfSpeech());
        unit.setType(request.getType());
        unit.setDifficulty(request.getDifficulty());
        unit.setLanguage(lang);
        unit.setCreatedAt(LocalDateTime.now());
        
        // Tạm thời bỏ qua việc tạo audio tự động để tránh timeout
        // Audio sẽ được tạo sau bằng cách gọi generate-audio endpoint
        System.out.println("📝 Lexicon unit created without audio: " + unit.getText());
        unit.setAudioUrl(null);
        
        return repository.save(unit);
    }

    public LexiconUnit update(Integer id, LexiconUnit updatedUnit) {
        LexiconUnit current = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        current.setText(updatedUnit.getText());
        current.setIpa(updatedUnit.getIpa());
        current.setImageUrl(updatedUnit.getImageUrl());
        current.setAudioUrl(updatedUnit.getAudioUrl());
        current.setMeaningEng(updatedUnit.getMeaningEng());
        current.setPartOfSpeech(updatedUnit.getPartOfSpeech());
        current.setType(updatedUnit.getType());
        current.setDifficulty(updatedUnit.getDifficulty());
        
        // Tự động tạo lại audio nếu text thay đổi
        if (!current.getText().equals(updatedUnit.getText()) && 
            (current.getAudioUrl() == null || current.getAudioUrl().isEmpty())) {
            try {
                String audioBase64 = textToSpeechService.synthesizeText(updatedUnit.getText(), 
                    current.getLanguage().getCode());
                String audioUrl = audioFileService.saveAudioFromBase64(audioBase64, updatedUnit.getText(), 
                    current.getLanguage().getCode());
                current.setAudioUrl(audioUrl);
            } catch (Exception e) {
                System.err.println("Failed to regenerate audio for lexicon unit: " + e.getMessage());
            }
        }
        
        return repository.save(current);
    }

    public LexiconUnit update(Integer id, LexiconUnitUpdateRequest request) {
        LexiconUnit current = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        // Cập nhật các field từ request
        if (request.getText() != null) current.setText(request.getText());
        if (request.getIpa() != null) current.setIpa(request.getIpa());
        if (request.getImageUrl() != null) current.setImageUrl(request.getImageUrl());
        if (request.getAudioUrl() != null) current.setAudioUrl(request.getAudioUrl());
        if (request.getMeaningEng() != null) current.setMeaningEng(request.getMeaningEng());
        if (request.getPartOfSpeech() != null) current.setPartOfSpeech(request.getPartOfSpeech());
        if (request.getType() != null) current.setType(request.getType());
        if (request.getDifficulty() != null) current.setDifficulty(request.getDifficulty());
        
        // Cập nhật language nếu có languageCode
        if (request.getLanguageCode() != null) {
            Language language = languageRepository.findByCode(request.getLanguageCode())
                    .orElseThrow(() -> new RuntimeException("Language not found with code: " + request.getLanguageCode()));
            current.setLanguage(language);
        }
        
        // Tự động tạo lại audio nếu text thay đổi
        if (request.getText() != null && !current.getText().equals(request.getText()) && 
            (current.getAudioUrl() == null || current.getAudioUrl().isEmpty())) {
            try {
                String audioBase64 = textToSpeechService.synthesizeText(request.getText(), 
                    current.getLanguage().getCode());
                String audioUrl = audioFileService.saveAudioFromBase64(audioBase64, request.getText(), 
                    current.getLanguage().getCode());
                current.setAudioUrl(audioUrl);
            } catch (Exception e) {
                System.err.println("Failed to regenerate audio for lexicon unit: " + e.getMessage());
            }
        }
        
        return repository.save(current);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<LexiconUnit> findByLanguage(String langCode) {
        return repository.findByLanguage_Code(langCode);
    }

    public LexiconUnit generateAudioForLexicon(Integer id) {
        LexiconUnit unit = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lexicon unit not found"));
        
        try {
            String audioBase64 = textToSpeechService.synthesizeText(unit.getText(), 
                unit.getLanguage().getCode());
            String audioUrl = audioFileService.saveAudioFromBase64(audioBase64, unit.getText(), 
                unit.getLanguage().getCode());
            unit.setAudioUrl(audioUrl);
            return repository.save(unit);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate audio: " + e.getMessage(), e);
        }
    }

    public LexiconUnit generateAudioWithVoice(Integer id, String voiceName) {
        LexiconUnit unit = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lexicon unit not found"));
        
        try {
            String audioBase64 = textToSpeechService.synthesizeTextWithVoice(unit.getText(), 
                unit.getLanguage().getCode(), voiceName);
            String audioUrl = audioFileService.saveAudioFromBase64(audioBase64, unit.getText(), 
                unit.getLanguage().getCode());
            unit.setAudioUrl(audioUrl);
            return repository.save(unit);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate audio with voice: " + e.getMessage(), e);
        }
    }


}
