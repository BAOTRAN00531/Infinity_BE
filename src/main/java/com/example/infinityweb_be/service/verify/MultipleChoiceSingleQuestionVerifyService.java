package com.example.infinityweb_be.service.verify;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.dto.question.student.submit.SingleQuestionSubmitDto;
import org.springframework.stereotype.Service;

@Service
public class MultipleChoiceSingleQuestionVerifyService extends AbstractQuestionVerifyService {

    @Override
    protected boolean isInvalidAnswerFormat(SingleQuestionSubmitDto dto) {
        return dto.getQuestionOptionIds().size() != 1;
    }

    @Override
    protected boolean isCorrectAnswer(Question question, SingleQuestionSubmitDto dto) {
        Integer selectedOptionId = dto.getQuestionOptionIds().get(0);
        return question.getOptions().stream().anyMatch(option -> option.isCorrect() && option.getId().equals(selectedOptionId));
    }
}
