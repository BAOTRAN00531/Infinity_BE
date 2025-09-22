package com.example.infinityweb_be.controller.student.Question;

// StudentQuizController.java

import com.example.infinityweb_be.domain.dto.question.student.StudentQuizQuestionDto;
import com.example.infinityweb_be.domain.dto.question.student.submit.QuizResultDto;
import com.example.infinityweb_be.domain.dto.question.student.submit.QuizSubmissionDto;
import com.example.infinityweb_be.domain.dto.question.student.submit.SingleQuestionSubmitDto;
import com.example.infinityweb_be.domain.dto.question.student.submit.SingleQuestionSubmitResponseDto;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.UserService;
import com.example.infinityweb_be.service.question.student.StudentQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/student/quiz")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentQuizController {

    private final StudentQuizService studentQuizService;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/lesson/{lessonId}/questions")
    public List<StudentQuizQuestionDto> getQuizQuestions(
            @PathVariable Integer lessonId,
            Principal principal) {
        Integer userId = userService.getUserIdFromPrincipal(principal);
        return studentQuizService.getQuizQuestionsForStudent(lessonId, userId);
    }

    @PostMapping("/lesson/{lessonId}/submit")
    public QuizResultDto submitQuiz(
            @PathVariable Integer lessonId,
            @RequestBody QuizSubmissionDto submission,
            Principal principal) {
        Integer userId = userService.getUserIdFromPrincipal(principal);

        // ✅ Truyền studentId đã lấy được vào service
        return studentQuizService.submitQuiz(lessonId, submission, userId);
    }


    @PostMapping("/question/{questionId}/submit")
    public ResponseEntity<SingleQuestionSubmitResponseDto> submitSingleQuiz(
            @RequestBody SingleQuestionSubmitDto submission,
            Principal principal) {
        Integer userId = userService.getUserIdFromPrincipal(principal);

        // ✅ Truyền studentId đã lấy được vào service
        return ResponseEntity.ok(studentQuizService.submitSingleQuestion(submission, userId));
    }

}