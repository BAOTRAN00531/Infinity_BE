package com.example.infinityweb_be.service.verify;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.dto.question.student.submit.SingleQuestionSubmitDto;

public abstract class AbstractQuestionVerifyService implements QuestionVerifyService {
    @Override
    public boolean isCorrect(Question question, SingleQuestionSubmitDto dto) {
        if (isInvalidAnswerFormat(dto)) {
            throw new IllegalArgumentException("Invalid answer format");
        }

        return isCorrectAnswer(question, dto);
    }

    protected abstract boolean isInvalidAnswerFormat(SingleQuestionSubmitDto dto);

    protected abstract boolean isCorrectAnswer(Question question, SingleQuestionSubmitDto dto);
}
