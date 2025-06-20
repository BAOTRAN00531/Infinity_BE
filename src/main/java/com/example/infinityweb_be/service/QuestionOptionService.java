package com.example.infinityweb_be.service;


import com.example.infinityweb_be.domain.QuestionOption;
import com.example.infinityweb_be.repository.QuestionOptionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionOptionService {

    private final QuestionOptionRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    private void setSessionContext(int adminId) {
        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :adminId")
                .setParameter("adminId", adminId)
                .executeUpdate();
    }

    @Transactional
    public QuestionOption create(QuestionOption option, int adminId) {
        setSessionContext(adminId);
        return repository.save(option);
    }

    @Transactional
    public QuestionOption update(Integer id, QuestionOption data, int adminId) {
        setSessionContext(adminId);
        QuestionOption option = repository.findById(id).orElseThrow();
        option.setOptionText(data.getOptionText());
        option.setCorrect(data.isCorrect());
        option.setPosition(data.getPosition());
        option.setImageUrl(data.getImageUrl());
        return repository.save(option);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<QuestionOption> getByQuestionId(Integer questionId) {
        return repository.findByQuestionId(questionId);
    }
}


