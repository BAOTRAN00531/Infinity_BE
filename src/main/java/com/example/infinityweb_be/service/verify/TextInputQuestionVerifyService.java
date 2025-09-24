package com.example.infinityweb_be.service.verify;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionAnswer;
import com.example.infinityweb_be.domain.dto.question.student.submit.SingleQuestionSubmitDto;
import org.springframework.stereotype.Service;

@Service
public class TextInputQuestionVerifyService extends AbstractQuestionVerifyService {

    @Override
    protected boolean isInvalidAnswerFormat(SingleQuestionSubmitDto dto) {
        return dto.getAnswerText() == null || dto.getAnswerText().trim().isEmpty();
    }

    @Override
    protected boolean isCorrectAnswer(Question question, SingleQuestionSubmitDto dto) {
        String userAnswer = dto.getAnswerText().trim();

        // Check against all possible correct answers for the question
        return question.getAnswers().stream()
                .anyMatch(answer -> isAnswerMatch(answer, userAnswer));
    }

    private boolean isAnswerMatch(QuestionAnswer correctAnswer, String userAnswer) {
        String correctAnswerText = correctAnswer.getAnswerText().trim();

        // Case-sensitive comparison by default
        // You can modify this logic based on the caseSensitive field if needed
        if (correctAnswer.isCaseSensitive()) {
            return correctAnswerText.equals(userAnswer);
        } else {
            return correctAnswerText.equalsIgnoreCase(userAnswer);
        }
    }
}