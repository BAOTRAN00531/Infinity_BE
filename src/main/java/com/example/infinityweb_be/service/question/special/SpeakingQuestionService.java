package com.example.infinityweb_be.service.question.special;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionAnswer;
import com.example.infinityweb_be.domain.dto.question.special.SpeakingQuestionDto;
import com.example.infinityweb_be.repository.question.QuestionRepository;
import com.example.infinityweb_be.repository.question.QuestionAnswerRepository;
import com.example.infinityweb_be.repository.question.QuestionTypeRepository;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SpeakingQuestionService {
    
    private final QuestionRepository questionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    
    public List<SpeakingQuestionDto> getAll() {
        return questionRepository.findByQuestionType_Id(6) // Speaking type
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public SpeakingQuestionDto getById(Integer id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        return convertToDto(question);
    }
    
    public List<SpeakingQuestionDto> getByLessonId(Integer lessonId) {
        return questionRepository.findByLesson_IdAndQuestionType_Id(lessonId, 6)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public SpeakingQuestionDto create(SpeakingQuestionDto dto, Integer adminId) {
        Question question = Question.builder()
                .questionText(dto.getQuestionText())
                .lesson(lessonRepository.findById(dto.getLessonId())
                        .orElseThrow(() -> new RuntimeException("Lesson not found")))
                .questionType(questionTypeRepository.findById(dto.getQuestionTypeId())
                        .orElseThrow(() -> new RuntimeException("Question type not found")))
                .difficulty(dto.getDifficulty())
                .points(dto.getPoints())
                .audioUrl(dto.getAudioUrl())
                .createdBy(userRepository.findById(adminId)
                        .orElseThrow(() -> new RuntimeException("User not found")))
                .createdAt(LocalDateTime.now())
                .build();
        
        question = questionRepository.save(question);
        
        // Save target sentence as answer
        QuestionAnswer answer = QuestionAnswer.builder()
                .question(question)
                .answerText(dto.getTargetSentence())
                    .isCaseSensitive(false)
                .position(1)
                .build();
        questionAnswerRepository.save(answer);
        
        return convertToDto(question);
    }
    
    public SpeakingQuestionDto update(Integer id, SpeakingQuestionDto dto, Integer adminId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        
        question.setQuestionText(dto.getQuestionText());
        question.setLesson(lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found")));
        question.setQuestionType(questionTypeRepository.findById(dto.getQuestionTypeId())
                .orElseThrow(() -> new RuntimeException("Question type not found")));
        question.setDifficulty(dto.getDifficulty());
        question.setPoints(dto.getPoints());
        question.setAudioUrl(dto.getAudioUrl());
        question.setUpdatedBy(userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found")));
        question.setUpdatedAt(LocalDateTime.now());
        
        // Update target sentence answer
        questionAnswerRepository.deleteByQuestion_Id(id);
        QuestionAnswer answer = QuestionAnswer.builder()
                .question(question)
                .answerText(dto.getTargetSentence())
                    .isCaseSensitive(false)
                .position(1)
                .build();
        questionAnswerRepository.save(answer);
        
        question = questionRepository.save(question);
        return convertToDto(question);
    }
    
    public void delete(Integer id) {
        questionRepository.deleteById(id);
    }
    
    public SpeakingQuestionDto validateSpeaking(Integer id, String userAudioUrl) {
        // TODO: Implement AI-based speaking validation
        // This would integrate with your existing speaking validation logic
        SpeakingQuestionDto question = getById(id);
        
        // Placeholder validation logic
        // In real implementation, you would:
        // 1. Call AI service to compare user audio with target sentence
        // 2. Return validation result with score/feedback
        
        return question;
    }
    
    private SpeakingQuestionDto convertToDto(Question question) {
        String targetSentence = question.getAnswers().stream()
                .findFirst()
                .map(QuestionAnswer::getAnswerText)
                .orElse("");
        
        return SpeakingQuestionDto.builder()
                .questionText(question.getQuestionText())
                .lessonId(question.getLesson().getId())
                .questionTypeId(question.getQuestionType().getId())
                .difficulty(question.getDifficulty())
                .points(question.getPoints())
                .targetSentence(targetSentence)
                .languageCode("en-US") // Default, should be stored in question
                .audioUrl(question.getAudioUrl())
                .pronunciationTips("") // Should be stored in question
                .timeLimit(30) // Default, should be stored in question
                .build();
    }
}
