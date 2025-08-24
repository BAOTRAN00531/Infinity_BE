package com.example.infinityweb_be.controller.student.Question;

// StudentQuizController.java

import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.dto.question.student.submit.QuizResultDto;
import com.example.infinityweb_be.domain.dto.question.student.submit.QuizSubmissionDto;
import com.example.infinityweb_be.domain.dto.question.student.StudentQuizQuestionDto;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.question.student.StudentQuizService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; // Thêm import
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/student/quiz")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentQuizController {

    private final StudentQuizService studentQuizService;
    private final UserRepository userRepository;

    @GetMapping("/lesson/{lessonId}/questions")
    public List<StudentQuizQuestionDto> getQuizQuestions(
            @PathVariable Integer lessonId,
            Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        Integer studentId = user.getId();

        return studentQuizService.getQuizQuestionsForStudent(lessonId, studentId);
    }

    @PostMapping("/lesson/{lessonId}/submit")
    public QuizResultDto submitQuiz(
            @PathVariable Integer lessonId,
            @RequestBody QuizSubmissionDto submission,
            // ✅ Sử dụng Authentication thay vì Principal
            Authentication authentication) {

        // ✅ Lấy username/email từ Authentication
        String username = authentication.getName();

        // ✅ Tìm user và lấy ID
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        Integer studentId = user.getId();

        // ✅ Truyền studentId đã lấy được vào service
        return studentQuizService.submitQuiz(lessonId, submission, studentId);
    }

}