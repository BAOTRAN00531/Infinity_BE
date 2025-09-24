package com.example.infinityweb_be.service.verify;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionOption;
import com.example.infinityweb_be.domain.dto.question.student.submit.SingleQuestionSubmitDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchingQuestionVerifyService extends AbstractQuestionVerifyService {

    @Override
    protected boolean isInvalidAnswerFormat(SingleQuestionSubmitDto dto) {
        List<Integer> selectedOptionIds = dto.getQuestionOptionIds();
        return selectedOptionIds == null || selectedOptionIds.isEmpty();
    }

    @Override
    protected boolean isCorrectAnswer(Question question, SingleQuestionSubmitDto dto) {
        List<Integer> selectedOptionIds = dto.getQuestionOptionIds();
        List<QuestionOption> questionOptions = question.getOptions();

        // For matching questions, user must select all options
        if (selectedOptionIds.size() != questionOptions.size()) {
            return false;
        }

        // Create a map of option ID to position for quick lookup
        Map<Integer, Integer> optionPositionMap = questionOptions.stream()
                .collect(Collectors.toMap(QuestionOption::getId, QuestionOption::getPosition));

        // Verify that selected options are in correct position order
        // First selected option should have position 1, second should have position 2, etc.
        for (int i = 0; i < selectedOptionIds.size(); i++) {
            Integer selectedOptionId = selectedOptionIds.get(i);
            Integer expectedPosition = i + 1; // Position should be 1, 2, 3, 4...
            Integer actualPosition = optionPositionMap.get(selectedOptionId);

            if (actualPosition == null || !actualPosition.equals(expectedPosition)) {
                return false;
            }
        }

        return true;
    }
}