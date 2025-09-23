package com.example.infinityweb_be.service.question.managment;

import com.example.infinityweb_be.domain.dto.enumm.QuestionType;
import com.example.infinityweb_be.repository.question.managment.AdminQuestionTypeHandler;
import com.example.infinityweb_be.request.QuestionUpsertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionCommandService {

    private final List<AdminQuestionTypeHandler> handlers;

    private AdminQuestionTypeHandler handler(String typeKey) {
        String key = typeKey == null ? "" : typeKey.trim().toLowerCase();
        return handlers.stream()
                .filter(h -> h.supportsKey().equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported question type: " + typeKey));
    }

    @Transactional
    public Long create(QuestionUpsertRequest req) {
        var h = handler(req.getType());
        h.validate(req);
        return h.create(req);
    }

    @Transactional
    public Long update(Long id, QuestionUpsertRequest req) {
        var h = handler(req.getType());
        h.validate(req);
        return h.update(id, req);
    }

    @Transactional
    public void delete(Long id) {
        // TODO: Xoá options/answers liên quan (cascade hoặc thủ công) rồi xoá question
        // questionRepository.deleteById(id);
    }
}