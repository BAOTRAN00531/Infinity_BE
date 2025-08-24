// QuestionAnswerService.java (Refactored to use DTOs)
package com.example.infinityweb_be.service.question;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionAnswer;
import com.example.infinityweb_be.domain.dto.question.admin.AnswerCreateDto;
import com.example.infinityweb_be.domain.dto.question.admin.AnswerResponseDto;
import com.example.infinityweb_be.repository.question.QuestionAnswerRepository;
import com.example.infinityweb_be.repository.question.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionAnswerService {

    private final QuestionAnswerRepository answerRepo;
    private final QuestionRepository questionRepo;

    @PersistenceContext
    private EntityManager entityManager;

    private void setSessionContext(int adminId) {
        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :adminId")
                .setParameter("adminId", adminId)
                .executeUpdate();
    }

    @Transactional
    public AnswerResponseDto create(AnswerCreateDto dto, int adminId) {
        setSessionContext(adminId);
        // Map DTO to entity
        Question question = questionRepo.getReferenceById(dto.getQuestionId());
        QuestionAnswer entity = new QuestionAnswer();
        entity.setQuestion(question);
        entity.setAnswerText(dto.getAnswerText());
        entity.setCaseSensitive(dto.isCaseSensitive());
        entity.setPosition(dto.getPosition());

        QuestionAnswer saved = answerRepo.save(entity);
        // Map entity to response DTO
        return AnswerResponseDto.builder()
                .id(saved.getId())
                .answerText(saved.getAnswerText())
                .caseSensitive(saved.isCaseSensitive())
                .position(saved.getPosition())
                .build();
    }

    @Transactional
    public AnswerResponseDto update(Integer id, AnswerCreateDto dto, int adminId) {
        setSessionContext(adminId);
        QuestionAnswer existing = answerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found: " + id));
        // Optionally update question association
        if (dto.getQuestionId() != null) {
            existing.setQuestion(questionRepo.getReferenceById(dto.getQuestionId()));
        }
        if (dto.getAnswerText() != null) existing.setAnswerText(dto.getAnswerText());
        existing.setCaseSensitive(dto.isCaseSensitive());
        if (dto.getPosition() != null) existing.setPosition(dto.getPosition());

        QuestionAnswer saved = answerRepo.save(existing);
        return AnswerResponseDto.builder()
                .id(saved.getId())
                .answerText(saved.getAnswerText())
                .caseSensitive(saved.isCaseSensitive())
                .position(saved.getPosition())
                .build();
    }

    @Transactional
    public void delete(Integer id) {
        answerRepo.deleteById(id);
    }

    @Transactional
    public List<AnswerResponseDto> getByQuestionId(Integer questionId) {
        return answerRepo.findByQuestionId(questionId)
                .stream()
                .map(a -> AnswerResponseDto.builder()
                        .id(a.getId())
                        .answerText(a.getAnswerText())
                        .caseSensitive(a.isCaseSensitive())
                        .position(a.getPosition())
                        .build())
                .collect(Collectors.toList());
    }
}
