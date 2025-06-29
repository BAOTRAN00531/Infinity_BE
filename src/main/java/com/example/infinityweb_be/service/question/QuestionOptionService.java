package com.example.infinityweb_be.service.question;

import com.example.infinityweb_be.domain.QuestionOption;
import com.example.infinityweb_be.domain.dto.question.OptionCreateDto;
import com.example.infinityweb_be.domain.dto.question.OptionResponseDto;
import com.example.infinityweb_be.repository.QuestionOptionRepository;
import com.example.infinityweb_be.repository.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionOptionService {

    private final QuestionOptionRepository repository;
    private final QuestionRepository questionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private void setSessionContext(int adminId) {
        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :adminId")
                .setParameter("adminId", adminId)
                .executeUpdate();
    }

    @Transactional
    public OptionResponseDto create(OptionCreateDto dto, int adminId) {
        setSessionContext(adminId);
        // Map DTO to entity and set question reference
        QuestionOption entity = new QuestionOption();
        entity.setQuestion(questionRepository.getReferenceById(dto.getQuestionId()));
        entity.setOptionText(dto.getOptionText());
        entity.setCorrect(dto.isCorrect());
        entity.setPosition(dto.getPosition());
        entity.setImageUrl(dto.getImageUrl());

        QuestionOption saved = repository.save(entity);
        // Map entity to response DTO
        return OptionResponseDto.builder()
                .id(saved.getId())
                .optionText(saved.getOptionText())
                .correct(saved.isCorrect())
                .position(saved.getPosition())
                .imageUrl(saved.getImageUrl())
                .build();
    }

    @Transactional
    public OptionResponseDto update(Integer id, OptionCreateDto dto, int adminId) {
        setSessionContext(adminId);
        QuestionOption existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Option not found: " + id));
        // Update question reference if needed
        if (dto.getQuestionId() != null) {
            existing.setQuestion(questionRepository.getReferenceById(dto.getQuestionId()));
        }
        // Update other fields
        if (dto.getOptionText() != null) existing.setOptionText(dto.getOptionText());
        existing.setCorrect(dto.isCorrect());
        if (dto.getPosition() != null) existing.setPosition(dto.getPosition());
        if (dto.getImageUrl() != null) existing.setImageUrl(dto.getImageUrl());

        QuestionOption saved = repository.save(existing);
        return OptionResponseDto.builder()
                .id(saved.getId())
                .optionText(saved.getOptionText())
                .correct(saved.isCorrect())
                .position(saved.getPosition())
                .imageUrl(saved.getImageUrl())
                .build();
    }

    @Transactional
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Transactional
    public List<OptionResponseDto> getByQuestionId(Integer questionId) {
        return repository.findByQuestionId(questionId)
                .stream()
                .map(o -> OptionResponseDto.builder()
                        .id(o.getId())
                        .optionText(o.getOptionText())
                        .correct(o.isCorrect())
                        .position(o.getPosition())
                        .imageUrl(o.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OptionResponseDto> createAll(List<OptionCreateDto> dtos, int adminId) {
        setSessionContext(adminId);

        return dtos.stream().map(dto -> {
            QuestionOption entity = new QuestionOption();
            entity.setQuestion(questionRepository.getReferenceById(dto.getQuestionId()));
            entity.setOptionText(dto.getOptionText());
            entity.setCorrect(dto.isCorrect());
            entity.setPosition(dto.getPosition());
            entity.setImageUrl(dto.getImageUrl());

            QuestionOption saved = repository.save(entity);

            return OptionResponseDto.builder()
                    .id(saved.getId())
                    .optionText(saved.getOptionText())
                    .correct(saved.isCorrect())
                    .position(saved.getPosition())
                    .imageUrl(saved.getImageUrl())
                    .build();
        }).collect(Collectors.toList());
    }

}
