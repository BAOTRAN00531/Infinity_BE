package com.example.infinityweb_be.service.verify;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionOption;
import com.example.infinityweb_be.domain.dto.question.student.submit.SingleQuestionSubmitDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MultipleChoiceMultiQuestionVerifyService extends AbstractQuestionVerifyService {
    @Override
    protected boolean isInvalidAnswerFormat(SingleQuestionSubmitDto dto) {
        return dto.getQuestionOptionIds().isEmpty();
    }

    @Override
    protected boolean isCorrectAnswer(Question question, SingleQuestionSubmitDto dto) {
        // Get all correct option IDs from the question
        Set<Integer> correctOptionIds = question.getOptions().stream()
                .filter(QuestionOption::isCorrect)
                .map(QuestionOption::getId)
                .collect(Collectors.toSet());

        // Get submitted option IDs from the DTO
        Set<Integer> submittedOptionIds = new HashSet<>(dto.getQuestionOptionIds());

        // For multiple choice multi-answer: the answer is correct only if
        // the submitted option IDs exactly match the correct option IDs
        return correctOptionIds.equals(submittedOptionIds);
    }
}
