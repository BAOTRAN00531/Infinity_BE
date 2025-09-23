package com.example.infinityweb_be.controller.special;

import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.dto.question.special.MatchingQuestionDto;
import com.example.infinityweb_be.service.question.special.MatchingQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions/matching")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class MatchingQuestionController {
    
    private final MatchingQuestionService matchingQuestionService;
    private final AuthHelper authHelper;
    
    @GetMapping
    public List<MatchingQuestionDto> getAll() {
        return matchingQuestionService.getAll();
    }
    
    @GetMapping("/{id}")
    public MatchingQuestionDto getById(@PathVariable Integer id) {
        return matchingQuestionService.getById(id);
    }
    
    @GetMapping("/lesson/{lessonId}")
    public List<MatchingQuestionDto> getByLesson(@PathVariable Integer lessonId) {
        return matchingQuestionService.getByLessonId(lessonId);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchingQuestionDto create(@Valid @RequestBody MatchingQuestionDto dto,
                                     JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return matchingQuestionService.create(dto, adminId);
    }
    
    @PostMapping("/test")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("permitAll()")
    public MatchingQuestionDto createTest(@Valid @RequestBody MatchingQuestionDto dto) {
        // Test endpoint không cần authentication
        return matchingQuestionService.create(dto, 1); // Dùng admin ID = 1 cho test
    }
    
    @PutMapping("/{id}")
    public MatchingQuestionDto update(@PathVariable Integer id,
                                     @Valid @RequestBody MatchingQuestionDto dto,
                                     JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return matchingQuestionService.update(id, dto, adminId);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        matchingQuestionService.delete(id);
    }
}
