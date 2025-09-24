package com.example.infinityweb_be.controller.special;

import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.dto.question.special.ImageChoiceQuestionDto;
import com.example.infinityweb_be.service.question.special.ImageChoiceQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions/image-choice")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ImageChoiceQuestionController {
    
    private final ImageChoiceQuestionService imageChoiceQuestionService;
    private final AuthHelper authHelper;
    
    @GetMapping
    public List<ImageChoiceQuestionDto> getAll() {
        return imageChoiceQuestionService.getAll();
    }
    
    @GetMapping("/{id}")
    public ImageChoiceQuestionDto getById(@PathVariable Integer id) {
        return imageChoiceQuestionService.getById(id);
    }
    
    @GetMapping("/lesson/{lessonId}")
    public List<ImageChoiceQuestionDto> getByLesson(@PathVariable Integer lessonId) {
        return imageChoiceQuestionService.getByLessonId(lessonId);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImageChoiceQuestionDto create(@Valid @RequestBody ImageChoiceQuestionDto dto,
                                        JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return imageChoiceQuestionService.create(dto, adminId);
    }
    
    @PutMapping("/{id}")
    public ImageChoiceQuestionDto update(@PathVariable Integer id,
                                        @Valid @RequestBody ImageChoiceQuestionDto dto,
                                        JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return imageChoiceQuestionService.update(id, dto, adminId);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        imageChoiceQuestionService.delete(id);
    }
}
