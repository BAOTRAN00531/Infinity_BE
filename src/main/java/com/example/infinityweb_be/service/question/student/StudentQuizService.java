package com.example.infinityweb_be.service.question.student;

// src/main/java/com/example/infinityweb_be/service/question/student/StudentQuizService.java

import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionOption;
import com.example.infinityweb_be.domain.dto.question.student.submit.QuizResult;
import com.example.infinityweb_be.domain.dto.question.student.submit.QuizResultDto;
import com.example.infinityweb_be.domain.dto.question.student.submit.QuizSubmissionDto;
import com.example.infinityweb_be.domain.dto.question.student.StudentQuizQuestionDto;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.enrollment.EnrollmentRepository;
import com.example.infinityweb_be.repository.question.QuestionRepository;
import com.example.infinityweb_be.repository.question.QuizResultRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentQuizService {

    private final QuestionRepository questionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final StudentQuizMapper studentQuizMapper;
    private final QuizResultRepository quizResultRepository;

    public List<StudentQuizQuestionDto> getQuizQuestionsForStudent(Integer lessonId, Integer studentId) {
        // 1. Tìm lesson để lấy courseId
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found with id: " + lessonId));

        Integer courseId = lesson.getModule().getCourse().getId();

        // 2. Kiểm tra enrollment ở cấp độ COURSE
        boolean isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(studentId, courseId);
        if (!isEnrolled) {
            throw new AccessDeniedException("Bạn chưa đăng ký khóa học chứa bài học này");
        }

        // 3. Lấy questions và map sang DTO an toàn
        return questionRepository.findByLesson_Id(lessonId).stream()
                .map(studentQuizMapper::toStudentQuizQuestionDto)
                .toList();
    }

    // ---

    public QuizResultDto submitQuiz(Integer lessonId, QuizSubmissionDto submission, Integer studentId) {
        // 1. Tìm lesson để lấy courseId, giống như phương thức get
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found with id: " + lessonId));

        Integer courseId = lesson.getModule().getCourse().getId();

        // 2. Kiểm tra enrollment ở cấp độ COURSE
        boolean isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(studentId, courseId);
        if (!isEnrolled) {
            throw new AccessDeniedException("Bạn chưa đăng ký khóa học chứa bài học này");
        }

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
}