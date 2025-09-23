package com.example.infinityweb_be.controller.special;

import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.dto.question.special.ListeningQuestionDto;
import com.example.infinityweb_be.service.question.special.ListeningQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions/listening")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ListeningQuestionController {
    
    private final ListeningQuestionService listeningQuestionService;
    private final AuthHelper authHelper;
    
    @GetMapping
    public List<ListeningQuestionDto> getAll() {
        return listeningQuestionService.getAll();
    }
    
    @GetMapping("/{id}")
    public ListeningQuestionDto getById(@PathVariable Integer id) {
        return listeningQuestionService.getById(id);
    }
    
    @GetMapping("/lesson/{lessonId}")
    public List<ListeningQuestionDto> getByLesson(@PathVariable Integer lessonId) {
        return listeningQuestionService.getByLessonId(lessonId);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ListeningQuestionDto create(@Valid @RequestBody ListeningQuestionDto dto,
                                      JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return listeningQuestionService.create(dto, adminId);
    }
    
    @PutMapping("/{id}")
    public ListeningQuestionDto update(@PathVariable Integer id,
                                      @Valid @RequestBody ListeningQuestionDto dto,
                                      JwtAuthenticationToken token) {
        int adminId = authHelper.getCurrentUserId(token);
        return listeningQuestionService.update(id, dto, adminId);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        listeningQuestionService.delete(id);
    }
}
