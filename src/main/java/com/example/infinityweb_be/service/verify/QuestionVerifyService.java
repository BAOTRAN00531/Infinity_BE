package com.example.infinityweb_be.service.verify;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.dto.question.student.submit.SingleQuestionSubmitDto;

public interface QuestionVerifyService {
    boolean isCorrect(Question question, SingleQuestionSubmitDto dto);
}
