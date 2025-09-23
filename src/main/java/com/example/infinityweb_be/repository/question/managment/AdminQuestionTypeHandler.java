package com.example.infinityweb_be.repository.question.managment;

import com.example.infinityweb_be.domain.QuestionType;
import com.example.infinityweb_be.request.QuestionUpsertRequest;

public interface AdminQuestionTypeHandler {
    // Khóa định danh type, ví dụ "single_choice"
    String supportsKey();

    void validate(QuestionUpsertRequest req);

    Long create(QuestionUpsertRequest req);

    Long update(Long id, QuestionUpsertRequest req);
}