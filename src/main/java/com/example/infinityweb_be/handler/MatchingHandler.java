package com.example.infinityweb_be.handler;

import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionType;
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
public class MatchingHandler implements AdminQuestionTypeHandler {

    private final QuestionRepository questionRepository;
    private final QuestionPayloadService payloadService;
    private final UserRepository userRepository;

    public MatchingHandler(QuestionRepository questionRepository,
                           QuestionPayloadService payloadService,
                           UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.payloadService = payloadService;
        this.userRepository = userRepository;
    }

    @Override public String supportsKey() { return "matching"; }

    @Override
    public void validate(QuestionUpsertRequest req) {
        Map<String, Object> p = req.getPayload();
        if (p == null || !p.containsKey("left") || !p.containsKey("right") || !p.containsKey("pairs")) {
            throw new IllegalArgumentException("payload.left, payload.right, payload.pairs are required for matching");
        }
        if (req.getActorUserId() == null) throw new IllegalArgumentException("actorUserId is required");
        if (req.getQuestionTypeId() == null) throw new IllegalArgumentException("questionTypeId is required");
    }

    @Override @Transactional
    public Long create(QuestionUpsertRequest req) {
        var actor = userRepository.findById(req.getActorUserId()).orElseThrow();
        var lessonRef = new Lesson(); lessonRef.setId(req.getLessonId());
        var typeRef = new QuestionType(); typeRef.setId(req.getQuestionTypeId());

        var q = Question.builder()
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

        payloadService.upsert(q, req.getPayload());
        return q.getId().longValue();
    }

    @Override @Transactional
    public Long update(Long id, QuestionUpsertRequest req) {
        var actor = userRepository.findById(req.getActorUserId()).orElseThrow();
        var q = questionRepository.findById(id.intValue()).orElseThrow();

        q.setQuestionText(req.getQuestionText());
        if (req.getDifficulty() != null) q.setDifficulty(req.getDifficulty());
        if (req.getPoints() != null) q.setPoints(req.getPoints());
        q.setMediaUrl(req.getMediaUrl());
        q.setAudioUrl(req.getAudioUrl());
        q.setVideoUrl(req.getVideoUrl());
        if (req.getLessonId() != null) { var l = new Lesson(); l.setId(req.getLessonId()); q.setLesson(l); }
        if (req.getQuestionTypeId() != null) { var t = new QuestionType(); t.setId(req.getQuestionTypeId()); q.setQuestionType(t); }

        q.setUpdatedBy(actor);
        q.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(q);

        payloadService.upsert(q, req.getPayload());
        return id;
    }
}
