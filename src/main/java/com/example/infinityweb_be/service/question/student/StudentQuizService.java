package com.example.infinityweb_be.service.question.student;

// src/main/java/com/example/infinityweb_be/service/question/student/StudentQuizService.java

import com.example.infinityweb_be.common.NotFoundException;
import com.example.infinityweb_be.constans.QuestionType;
import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionOption;
import com.example.infinityweb_be.domain.dto.question.student.StudentQuizQuestionDto;
import com.example.infinityweb_be.domain.dto.question.student.submit.QuizResult;
import com.example.infinityweb_be.domain.dto.question.student.submit.QuizResultDto;
import com.example.infinityweb_be.domain.dto.question.student.submit.QuizSubmissionDto;
import com.example.infinityweb_be.domain.dto.question.student.submit.SingleQuestionSubmitDto;
import com.example.infinityweb_be.domain.dto.question.student.submit.SingleQuestionSubmitResponseDto;
import com.example.infinityweb_be.entity.UserQuestionProgressEntity;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.repository.question.QuestionRepository;
import com.example.infinityweb_be.repository.question.QuizResultRepository;
import com.example.infinityweb_be.repository.question.UserQuestionProgressRepository;
import com.example.infinityweb_be.service.Enrollment.EnrollmentService;
import com.example.infinityweb_be.service.verify.MultipleChoiceMultiQuestionVerifyService;
import com.example.infinityweb_be.service.verify.MultipleChoiceSingleQuestionVerifyService;
import com.example.infinityweb_be.service.verify.TextInputQuestionVerifyService;
import com.example.infinityweb_be.service.verify.FillInTheBlankQuestionVerifyService;
import com.example.infinityweb_be.service.verify.MatchingQuestionVerifyService;
import com.example.infinityweb_be.service.verify.QuestionVerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentQuizService {

    private final QuestionRepository questionRepository;
    private final EnrollmentService enrollmentService;
    private final StudentQuizMapper studentQuizMapper;
    private final QuizResultRepository quizResultRepository;
    private final UserQuestionProgressRepository userQuestionProgressRepository;
    private final MultipleChoiceSingleQuestionVerifyService multipleChoiceSingleQuestionVerifyService;
    private final MultipleChoiceMultiQuestionVerifyService multipleChoiceMultiQuestionVerifyService;
    private final TextInputQuestionVerifyService textInputQuestionVerifyService;
    private final FillInTheBlankQuestionVerifyService fillInTheBlankQuestionVerifyService;
    private final MatchingQuestionVerifyService matchingQuestionVerifyService;
    private final UserRepository userRepository;

    public List<StudentQuizQuestionDto> getQuizQuestionsForStudent(Integer lessonId, Integer studentId) {
        enrollmentService.verifyEnrolledLesson(studentId, lessonId);
        Set<Integer> completedQuestions = userQuestionProgressRepository.findByUser_IdAndLesson_IdAndIsCompleted(studentId, lessonId, true)
                .stream().map(it -> it.getQuestion().getId()).collect(Collectors.toSet());
        // 3. Lấy questions và map sang DTO an toàn
        return questionRepository.findByLesson_Id(lessonId).stream()
                .map(question -> toStudentQuizQuestionDto(question, completedQuestions))
                .toList();
    }

    private StudentQuizQuestionDto toStudentQuizQuestionDto(Question question, Set<Integer> completedQuestions) {
        StudentQuizQuestionDto dto = studentQuizMapper.toStudentQuizQuestionDto(question);
        dto.setCompleted(completedQuestions.contains(dto.getId()));
        return dto;
    }


    // ---

    public QuizResultDto submitQuiz(Integer lessonId, QuizSubmissionDto submission, Integer studentId) {
        enrollmentService.verifyEnrolledLesson(studentId, lessonId);
        // 3. Lấy tất cả questions của lesson
        List<Question> questions = questionRepository.findByLesson_Id(lessonId);

        // 4. Chấm điểm
        int totalQuestions = questions.size();
        int correctAnswers = 0;
        Map<Integer, Boolean> results = new HashMap<>();

        for (Question question : questions) {
            Integer selectedOptionId = submission.getAnswers().get(question.getId());
            boolean isCorrect = isAnswerCorrect(question, selectedOptionId);

            if (isCorrect) {
                correctAnswers++;
            }
            results.put(question.getId(), isCorrect);

            // 5. Lưu kết quả vào database
            saveQuizResult(studentId, question.getId(), selectedOptionId, isCorrect);
        }

        // 6. Tính điểm
        double score = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;

        // 7. Trả về kết quả
        return QuizResultDto.builder()
                .totalQuestions(totalQuestions)
                .correctAnswers(correctAnswers)
                .score(score)
                .results(results)
                .build();
    }

    private boolean isAnswerCorrect(Question question, Integer selectedOptionId) {
        if (selectedOptionId == null) return false;

        return question.getOptions().stream()
                .filter(QuestionOption::isCorrect)
                .anyMatch(option -> option.getId().equals(selectedOptionId));
    }

    private void saveQuizResult(Integer studentId, Integer questionId, Integer selectedOptionId, Boolean isCorrect) {
        QuizResult result = new QuizResult();
        result.setUserId(studentId);
        result.setQuestionId(questionId);
        result.setSelectedOptionId(selectedOptionId);
        result.setIsCorrect(isCorrect);
        result.setAnsweredAt(LocalDateTime.now());

        quizResultRepository.save(result);
    }

    public SingleQuestionSubmitResponseDto submitSingleQuestion(SingleQuestionSubmitDto dto, Integer userId) {
        Question question = questionRepository.findById(dto.getQuestionId()).orElseThrow(() ->
                new NotFoundException("Question with id " + dto.getQuestionId() + " not found")
        );
        boolean isCorrect = getQuestionVerifyService(question.getQuestionType().getCode()).isCorrect(question, dto);
        if (isCorrect) {
            userQuestionProgressRepository.findByUser_IdAndQuestion_Id(userId, question.getId())
                    .ifPresentOrElse(it -> {
                        it.setCompleted(true);
                        it.setCompletedAt(LocalDateTime.now());
                        userQuestionProgressRepository.save(it);
                    }, () -> {
                        UserQuestionProgressEntity entity = UserQuestionProgressEntity.builder()
                                .user(userRepository.findById(userId).orElse(null))
                                .module(question.getLesson().getModule())
                                .lesson(question.getLesson())
                                .question(question)
                                .isCompleted(true)
                                .completedAt(LocalDateTime.now())
                                .build();
                        userQuestionProgressRepository.save(entity);
                    });
        }
        return SingleQuestionSubmitResponseDto.builder().points(question.getPoints()).isCorrect(isCorrect).build();

    }

    private QuestionVerifyService getQuestionVerifyService(String questionTypeCode) {
        QuestionType questionType = QuestionType.getByCode(questionTypeCode);
        return switch (questionType) {
            case MULTIPLE_CHOICE_SINGLE -> multipleChoiceSingleQuestionVerifyService;
            case MULTIPLE_SINGLE_MULTI -> multipleChoiceMultiQuestionVerifyService;
            case TEXT_INPUT -> textInputQuestionVerifyService;
            case FILL_IN_THE_BLANK -> fillInTheBlankQuestionVerifyService;
            case MATCHING -> matchingQuestionVerifyService;
        };
    }


}