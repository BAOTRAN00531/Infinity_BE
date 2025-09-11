package com.example.infinityweb_be.service.question;

import com.example.infinityweb_be.domain.dto.question.admin.QuestionCreateDto;
import com.example.infinityweb_be.domain.dto.question.admin.QuestionResponseDto;

import java.util.List;

public interface QuestionService {
    QuestionResponseDto getById(Integer id);
    List<QuestionResponseDto> getByLessonId(Integer lessonId);
    QuestionResponseDto create(QuestionCreateDto dto, Integer adminId);



    QuestionResponseDto update(Integer id, QuestionCreateDto dto, Integer adminId);
    void delete(Integer id);
    void validateQuestionBeforeUse(Integer questionId);

    List<QuestionResponseDto> getAll();

    ;
}
