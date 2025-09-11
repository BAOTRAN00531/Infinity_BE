package com.example.infinityweb_be.service.question.student;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionOption;
import com.example.infinityweb_be.domain.QuestionAnswer;
import com.example.infinityweb_be.domain.dto.question.student.StudentQuizAnswerDto;
import com.example.infinityweb_be.domain.dto.question.student.StudentQuizOptionDto;
import com.example.infinityweb_be.domain.dto.question.student.StudentQuizQuestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentQuizMapper {

    @Mapping(source = "questionType.code", target = "type") // ✅ MAP code thành type
    @Mapping(target = "options", expression = "java(mapOptionsWithoutCorrect(question.getOptions()))")
    @Mapping(target = "answers", expression = "java(mapAnswersWithoutSensitive(question.getAnswers()))")
    StudentQuizQuestionDto toStudentQuizQuestionDto(Question question);

    default List<StudentQuizOptionDto> mapOptionsWithoutCorrect(List<QuestionOption> options) {
        if (options == null) return null;
        return options.stream()
                .map(opt -> {
                    StudentQuizOptionDto dto = new StudentQuizOptionDto();
                    dto.setId(opt.getId());
                    dto.setOptionText(opt.getOptionText());
                    dto.setPosition(opt.getPosition());
                    dto.setImageUrl(opt.getImageUrl());
                    return dto;
                })
                .toList();
    }

    default List<StudentQuizAnswerDto> mapAnswersWithoutSensitive(List<QuestionAnswer> answers) {
        if (answers == null) return null;
        return answers.stream()
                .map(ans -> {
                    StudentQuizAnswerDto dto = new StudentQuizAnswerDto();
                    dto.setId(ans.getId());
                    dto.setAnswerText(ans.getAnswerText());
                    dto.setPosition(ans.getPosition());
                    return dto;
                })
                .toList();
    }
}