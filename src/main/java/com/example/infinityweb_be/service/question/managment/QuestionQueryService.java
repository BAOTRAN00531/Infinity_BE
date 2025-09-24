package com.example.infinityweb_be.service.question.managment;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionOption;
import com.example.infinityweb_be.domain.dto.question.managment.QuestionOptionDto;
import com.example.infinityweb_be.repository.question.QuestionOptionRepository;
import com.example.infinityweb_be.repository.question.QuestionRepository;
import com.example.infinityweb_be.request.QuestionDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class QuestionQueryService {
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final QuestionPayloadService questionPayloadService;

    @Transactional(readOnly = true)
    public QuestionDetailResponse getDetail(Long id) {
        Question q = questionRepository.findById(id.intValue())
                .orElseThrow(() -> new IllegalArgumentException("Question not found: " + id));

        var dto = new QuestionDetailResponse();
        dto.setId(q.getId());
        dto.setLessonId(q.getLesson() != null ? q.getLesson().getId() : null);
        dto.setQuestionTypeId(q.getQuestionType() != null ? q.getQuestionType().getId() : null);
        dto.setQuestionText(q.getQuestionText());
        dto.setMediaUrl(q.getMediaUrl());
        dto.setAudioUrl(q.getAudioUrl());
        dto.setVideoUrl(q.getVideoUrl());
        dto.setDifficulty(q.getDifficulty());
        dto.setPoints(q.getPoints());

        // payload từ bảng Question_Payloads
        Map<String, Object> payload = questionPayloadService.load(q.getId());
        dto.setPayload(payload);

        // options (nếu có)
        List<QuestionOption> opts = optionRepository.findByQuestionId(q.getId());
        List<QuestionOptionDto> optionDtos = opts.stream().map(o -> {
            var od = new QuestionOptionDto();
            od.setId(o.getId() != null ? o.getId().longValue() : null);
            od.setContentText(o.getOptionText());
            od.setImageUrl(o.getImageUrl());
            od.setIsCorrect(o.isCorrect());
            od.setOrderIndex(o.getPosition());
            return od;
        }).toList();
        dto.setOptions(optionDtos);

        return dto;
    }
    @Transactional(readOnly = true)
    public Object listByLesson(Integer lessonId) {
        var list = questionRepository.findByLesson_Id(lessonId);
        return list.stream().map(q -> getDetail(q.getId().longValue())).toList();
    }

    @Transactional(readOnly = true)
    public Object listByLessonAndType(Integer lessonId, Integer questionTypeId) {
        var list = questionRepository.findByLesson_IdAndQuestionType_Id(lessonId, questionTypeId);
        return list.stream().map(q -> getDetail(q.getId().longValue())).toList();
    }
}