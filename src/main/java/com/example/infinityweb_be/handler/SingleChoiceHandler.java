package com.example.infinityweb_be.handler;

import com.example.infinityweb_be.domain.*;

import com.example.infinityweb_be.domain.dto.question.managment.QuestionOptionDto;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.repository.question.QuestionOptionRepository;
import com.example.infinityweb_be.repository.question.QuestionRepository;
import com.example.infinityweb_be.repository.question.managment.AdminQuestionTypeHandler;
import com.example.infinityweb_be.request.QuestionUpsertRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SingleChoiceHandler implements AdminQuestionTypeHandler {

    private final ObjectMapper objectMapper; // chưa dùng ở type này nhưng để đồng bộ
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final UserRepository userRepository;

    public SingleChoiceHandler(ObjectMapper objectMapper,
                               QuestionRepository questionRepository,
                               QuestionOptionRepository optionRepository,
                               UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String supportsKey() {
        return "single_choice";
    }

    @Override
    public void validate(QuestionUpsertRequest req) {
        List<QuestionOptionDto> options = (req.getOptions() != null) ? req.getOptions() : List.of();
        if (options.isEmpty()) {
            throw new IllegalArgumentException("Options are required for single_choice");
        }
        long correct = options.stream().filter(o -> Boolean.TRUE.equals(o.getIsCorrect())).count();
        if (correct != 1) {
            throw new IllegalArgumentException("Exactly one correct option is required");
        }
        if (req.getActorUserId() == null) {
            throw new IllegalArgumentException("actorUserId is required");
        }
        if (req.getQuestionTypeId() == null) {
            throw new IllegalArgumentException("questionTypeId is required");
        }
    }

    @Override
    @Transactional
    public Long create(QuestionUpsertRequest req) {
        User actor = userRepository.findById(req.getActorUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.getActorUserId()));

        // build stub refs cho Lesson & QuestionType chỉ cần id
        Lesson lessonRef = new Lesson();
        lessonRef.setId(req.getLessonId());

        QuestionType typeRef = new QuestionType();
        typeRef.setId(req.getQuestionTypeId());

        Question q = Question.builder()
                .lesson(lessonRef)
                .questionType(typeRef)
                .questionText(req.getQuestionText())
                .mediaUrl(req.getMediaUrl())
                .audioUrl(req.getAudioUrl())
                .videoUrl(req.getVideoUrl())
                .difficulty(Optional.ofNullable(req.getDifficulty()).orElse("EASY"))
                .points(Optional.ofNullable(req.getPoints()).orElse(10))
                .createdBy(actor)
                .createdAt(LocalDateTime.now())
                .build();

        questionRepository.save(q);

        int idx = 1;
        for (QuestionOptionDto o : req.getOptions()) {
            QuestionOption ent = new QuestionOption();
            ent.setQuestion(q);
            ent.setOptionText(o.getContentText());
            ent.setImageUrl(o.getImageUrl());
            ent.setCorrect(Boolean.TRUE.equals(o.getIsCorrect()));
            ent.setPosition(o.getOrderIndex() != null ? o.getOrderIndex() : idx++);
            optionRepository.save(ent);
        }
        return q.getId().longValue();
    }

    @Override
    @Transactional
    public Long update(Long id, QuestionUpsertRequest req) {
        User actor = userRepository.findById(req.getActorUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.getActorUserId()));

        Question q = questionRepository.findById(id.intValue()).orElseThrow();

        // cập nhật các field cho phép
        q.setQuestionText(req.getQuestionText());
        q.setMediaUrl(req.getMediaUrl());
        q.setAudioUrl(req.getAudioUrl());
        q.setVideoUrl(req.getVideoUrl());
        if (req.getDifficulty() != null) q.setDifficulty(req.getDifficulty());
        if (req.getPoints() != null) q.setPoints(req.getPoints());

        // cập nhật type/lesson nếu cần
        if (req.getLessonId() != null) {
            Lesson lessonRef = new Lesson(); lessonRef.setId(req.getLessonId());
            q.setLesson(lessonRef);
        }
        if (req.getQuestionTypeId() != null) {
            QuestionType typeRef = new QuestionType(); typeRef.setId(req.getQuestionTypeId());
            q.setQuestionType(typeRef);
        }

        q.setUpdatedBy(actor);
        q.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(q);

        // đồng bộ options
        Map<Integer, QuestionOption> existing = optionRepository.findByQuestionId(q.getId())
                .stream().collect(Collectors.toMap(QuestionOption::getId, it -> it));

        Set<Integer> keep = new HashSet<>();
        int idx = 1;
        if (req.getOptions() != null) {
            for (QuestionOptionDto o : req.getOptions()) {
                QuestionOption ent;
                if (o.getId() != null && existing.containsKey(o.getId().intValue())) {
                    ent = existing.get(o.getId().intValue());
                } else {
                    ent = new QuestionOption();
                    ent.setQuestion(q);
                }
                ent.setOptionText(o.getContentText());
                ent.setImageUrl(o.getImageUrl());
                ent.setCorrect(Boolean.TRUE.equals(o.getIsCorrect()));
                ent.setPosition(o.getOrderIndex() != null ? o.getOrderIndex() : idx++);
                optionRepository.save(ent);
                keep.add(ent.getId());
            }
        }

        // xoá option không còn
        for (QuestionOption ent : existing.values()) {
            if (!keep.contains(ent.getId())) {
                optionRepository.delete(ent);
            }
        }
        return id;
    }
}