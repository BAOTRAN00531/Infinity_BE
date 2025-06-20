package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.QuestionAnswer;
import com.example.infinityweb_be.repository.QuestionAnswerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionAnswerService {

    private final QuestionAnswerRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    private void setSessionContext(int adminId) {
        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :adminId")
                .setParameter("adminId", adminId)
                .executeUpdate();
    }

    @Transactional
    public QuestionAnswer create(QuestionAnswer answer, int adminId) {
        setSessionContext(adminId);
        return repository.save(answer);
    }

    public List<QuestionAnswer> getByQuestionId(Integer questionId) {
        return repository.findByQuestionId(questionId);
    }

    @Transactional
    public QuestionAnswer update(Integer id, QuestionAnswer newData, int adminId) {
        setSessionContext(adminId);
        QuestionAnswer old = repository.findById(id).orElseThrow();
        old.setAnswerText(newData.getAnswerText());
        old.setCaseSensitive(newData.isCaseSensitive());
        old.setPosition(newData.getPosition());
        return repository.save(old);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
