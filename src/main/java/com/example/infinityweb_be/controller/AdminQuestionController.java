package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.domain.dto.LessonDto;
import com.example.infinityweb_be.domain.dto.question.QuestionCreateDto;
import com.example.infinityweb_be.domain.dto.question.QuestionResponseDto;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.LessonService;
import com.example.infinityweb_be.service.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminQuestionController {
    private final QuestionService questionService;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final AuthHelper authHelper;
    private final LessonService lessonService;

    @GetMapping("/{id}")
    public QuestionResponseDto getById(@PathVariable Integer id) {
        return questionService.getById(id);
    }

    @GetMapping
    public List<QuestionResponseDto> getByLesson(@RequestParam Integer lessonId) {
        return questionService.getByLessonId(lessonId);
    }

    @GetMapping("/by-module/{moduleId}")
    public List<LessonDto> getLessonsByModule(@PathVariable Integer moduleId) {
        return lessonService.getByModuleIdDto(moduleId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<QuestionResponseDto> getAllQuestions() {
        return questionService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponseDto create(@Valid @RequestBody QuestionCreateDto dto,
                                      JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return questionService.create(dto, adminId);
    }

    @PutMapping("/{id}")
    public QuestionResponseDto update(@PathVariable Integer id,
                                      @Valid @RequestBody QuestionCreateDto dto,
                                      JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return questionService.update(id, dto, adminId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        questionService.delete(id);
    }

    @PostMapping("/{id}/validate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validate(@PathVariable Integer id) {
        questionService.validateQuestionBeforeUse(id);
    }
}



//// src/main/java/com/example/infinityweb_be/controller/AdminQuestionController.java
//package com.example.infinityweb_be.controller;
//
//import com.example.infinityweb_be.domain.dto.QuestionDto;
//import com.example.infinityweb_be.domain.Lesson;
//import com.example.infinityweb_be.repository.LessonRepository;
//import com.example.infinityweb_be.repository.UserRepository;
//import com.example.infinityweb_be.service.question.QuestionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/questions")
//@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
//public class AdminQuestionController {
//
//    private final QuestionService       questionService;
//    private final UserRepository        userRepository;
//    private final LessonRepository      lessonRepository;
//
//    /** 1) Lấy chi tiết question đầy đủ (kèm options/answers) */
//    @GetMapping("/{id}")
//    public QuestionDto getDetail(@PathVariable Integer id) {
//        return questionService.getDetail(id);
//    }
//
//    /** 2) Lấy list question đơn giản theo lessonId */
//    @GetMapping
//    public List<QuestionDto> getByLesson(@RequestParam Integer lessonId) {
//        return questionService
//                .getByLessonId(lessonId)
//                .stream()
//                .map(q -> questionService.getDetail(q.getId()))
//                .toList();
//    }
//
//    /** 3) Lấy lessons của module (giúp UI đổ dropdown lesson) */
//    @GetMapping("/by-module/{moduleId}")
//    public List<Lesson> getLessonsByModule(@PathVariable Integer moduleId) {
//        return lessonRepository.findByModule_Id(moduleId);
//    }
//
//    /** 4) Tạo mới “full question” (kèm options & answers) */
//    @PostMapping("/full")
//    @ResponseStatus(HttpStatus.CREATED)
//    public QuestionDto createFull(@RequestBody QuestionDto dto, JwtAuthenticationToken token) {
//        int adminId = userRepository
//                .findByEmail(token.getName())
//                .orElseThrow()
//                .getId();
//        return questionService.createFull(dto, adminId);
//    }
//
//    /** 5) Cập nhật “full question” */
//    @PutMapping("/full/{id}")
//    public QuestionDto updateFull(@PathVariable Integer id,
//                                  @RequestBody QuestionDto dto,
//                                  JwtAuthenticationToken token) {
//        int adminId = userRepository
//                .findByEmail(token.getName())
//                .orElseThrow()
//                .getId();
//        return questionService.updateFull(id, dto, adminId);
//    }
//
//    /** 6) Validate trước khi publish/preview */
//    @PostMapping("/{id}/validate")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void validate(@PathVariable Integer id) {
//        questionService.validateQuestionBeforeUse(id);
//    }
//
//    /** 7) Xoá question */
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void delete(@PathVariable Integer id) {
//        questionService.delete(id);
//    }
//}
