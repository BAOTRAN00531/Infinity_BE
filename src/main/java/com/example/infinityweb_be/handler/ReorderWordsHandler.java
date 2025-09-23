package com.example.infinityweb_be.handler;

import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionType;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.repository.question.QuestionRepository;
import com.example.infinityweb_be.repository.question.managment.AdminQuestionTypeHandler;
import com.example.infinityweb_be.request.QuestionUpsertRequest;
import com.example.infinityweb_be.service.question.managment.QuestionPayloadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Component
public class ReorderWordsHandler implements AdminQuestionTypeHandler {

    private final QuestionRepository questionRepository;
    private final QuestionPayloadService questionPayloadService;
    private final UserRepository userRepository;

    public ReorderWordsHandler(QuestionRepository questionRepository,
                               QuestionPayloadService questionPayloadService,
                               UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.questionPayloadService = questionPayloadService;
        this.userRepository = userRepository;
    }

    @Override public String supportsKey() { return "reorder_words"; }

    @Override
    public void validate(QuestionUpsertRequest req) {
        Map<String, Object> p = req.getPayload();
        if (p == null || !p.containsKey("tokens") || !p.containsKey("answer")) {
            throw new IllegalArgumentException("payload.tokens and payload.answer are required");
        }
        if (req.getActorUserId() == null) throw new IllegalArgumentException("actorUserId is required");
        if (req.getQuestionTypeId() == null) throw new IllegalArgumentException("questionTypeId is required");
    }

    @Override @Transactional
    public Long create(QuestionUpsertRequest req) {
        User actor = userRepository.findById(req.getActorUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.getActorUserId()));

        Lesson lessonRef = new Lesson(); lessonRef.setId(req.getLessonId());
        QuestionType typeRef = new QuestionType(); typeRef.setId(req.getQuestionTypeId());

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

        questionPayloadService.upsert(q, req.getPayload());
        return q.getId().longValue();
    }

    @Override @Transactional
    public Long update(Long id, QuestionUpsertRequest req) {
        User actor = userRepository.findById(req.getActorUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.getActorUserId()));

        Question q = questionRepository.findById(id.intValue()).orElseThrow();

        q.setQuestionText(req.getQuestionText());
        if (req.getDifficulty() != null) q.setDifficulty(req.getDifficulty());
        if (req.getPoints() != null) q.setPoints(req.getPoints());
        q.setMediaUrl(req.getMediaUrl());
        q.setAudioUrl(req.getAudioUrl());
        q.setVideoUrl(req.getVideoUrl());

        if (req.getLessonId() != null) { Lesson l = new Lesson(); l.setId(req.getLessonId()); q.setLesson(l); }
        if (req.getQuestionTypeId() != null) { QuestionType t = new QuestionType(); t.setId(req.getQuestionTypeId()); q.setQuestionType(t); }

        q.setUpdatedBy(actor);
        q.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(q);

        questionPayloadService.upsert(q, req.getPayload());
        return id;
    }
}
