package com.example.infinityweb_be.controller.special;

import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.dto.question.special.FillInTheBlankQuestionDto;
import com.example.infinityweb_be.service.question.special.FillInTheBlankQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions/fill-in-the-blank")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class FillInTheBlankQuestionController {
    
    private final FillInTheBlankQuestionService fillInTheBlankQuestionService;
    private final AuthHelper authHelper;
    
    @GetMapping
    public List<FillInTheBlankQuestionDto> getAll() {
        return fillInTheBlankQuestionService.getAll();
    }
    
    @GetMapping("/{id}")
    public FillInTheBlankQuestionDto getById(@PathVariable Integer id) {
        return fillInTheBlankQuestionService.getById(id);
    }
    
    @GetMapping("/lesson/{lessonId}")
    public List<FillInTheBlankQuestionDto> getByLesson(@PathVariable Integer lessonId) {
        return fillInTheBlankQuestionService.getByLessonId(lessonId);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FillInTheBlankQuestionDto create(@Valid @RequestBody FillInTheBlankQuestionDto dto,
                                           JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return fillInTheBlankQuestionService.create(dto, adminId);
    }
    
    @PutMapping("/{id}")
    public FillInTheBlankQuestionDto update(@PathVariable Integer id,
                                           @Valid @RequestBody FillInTheBlankQuestionDto dto,
                                           JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return fillInTheBlankQuestionService.update(id, dto, adminId);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        fillInTheBlankQuestionService.delete(id);
    }
}
