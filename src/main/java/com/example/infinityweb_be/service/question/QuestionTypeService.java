package com.example.infinityweb_be.service.question;

import com.example.infinityweb_be.domain.QuestionType;
import com.example.infinityweb_be.repository.question.QuestionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionTypeService {

    private final QuestionTypeRepository repository;

    public List<QuestionType> getAll() {
        return repository.findAll();
    }

    public QuestionType create(QuestionType type) {
        return repository.save(type);
    }

    public QuestionType update(Integer id, QuestionType data) {
        QuestionType q = repository.findById(id).orElseThrow();
        q.setCode(data.getCode());
        q.setDescription(data.getDescription());
        q.setMinOptions(data.getMinOptions());
        q.setMinCorrect(data.getMinCorrect());
        q.setMaxCorrect(data.getMaxCorrect());
        return repository.save(q);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}

