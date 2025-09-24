package com.example.infinityweb_be.controller.special;

import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.dto.question.special.WordOrderQuestionDto;
import com.example.infinityweb_be.service.question.special.WordOrderQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions/word-order")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class WordOrderQuestionController {
    
    private final WordOrderQuestionService wordOrderQuestionService;
    private final AuthHelper authHelper;
    
    @GetMapping
    public List<WordOrderQuestionDto> getAll() {
        return wordOrderQuestionService.getAll();
    }
    
    @GetMapping("/{id}")
    public WordOrderQuestionDto getById(@PathVariable Integer id) {
        return wordOrderQuestionService.getById(id);
    }
    
    @GetMapping("/lesson/{lessonId}")
    public List<WordOrderQuestionDto> getByLesson(@PathVariable Integer lessonId) {
        return wordOrderQuestionService.getByLessonId(lessonId);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WordOrderQuestionDto create(@Valid @RequestBody WordOrderQuestionDto dto,
                                      JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return wordOrderQuestionService.create(dto, adminId);
    }
    
    @PutMapping("/{id}")
    public WordOrderQuestionDto update(@PathVariable Integer id,
                                      @Valid @RequestBody WordOrderQuestionDto dto,
                                      JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return wordOrderQuestionService.update(id, dto, adminId);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        wordOrderQuestionService.delete(id);
    }
}
