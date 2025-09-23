package com.example.infinityweb_be.service.question.special;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionAnswer;
import com.example.infinityweb_be.domain.dto.question.special.FillInTheBlankQuestionDto;
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
public class FillInTheBlankQuestionService {
    
    private final QuestionRepository questionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    
    public List<FillInTheBlankQuestionDto> getAll() {
        return questionRepository.findByQuestionType_Id(5) // Fill in the Blank type
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public FillInTheBlankQuestionDto getById(Integer id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        return convertToDto(question);
    }
    
    public List<FillInTheBlankQuestionDto> getByLessonId(Integer lessonId) {
        return questionRepository.findByLesson_IdAndQuestionType_Id(lessonId, 5)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public FillInTheBlankQuestionDto create(FillInTheBlankQuestionDto dto, Integer adminId) {
        Question question = Question.builder()
                .questionText(dto.getQuestionText())
                .lesson(lessonRepository.findById(dto.getLessonId())
                        .orElseThrow(() -> new RuntimeException("Lesson not found")))
                .questionType(questionTypeRepository.findById(dto.getQuestionTypeId())
                        .orElseThrow(() -> new RuntimeException("Question type not found")))
                .difficulty(dto.getDifficulty())
                .points(dto.getPoints())
                .createdBy(userRepository.findById(adminId)
                        .orElseThrow(() -> new RuntimeException("User not found")))
                .createdAt(LocalDateTime.now())
                .build();
        
        question = questionRepository.save(question);
        
        // Save correct answers
        for (int i = 0; i < dto.getCorrectAnswers().size(); i++) {
            QuestionAnswer answer = QuestionAnswer.builder()
                    .question(question)
                    .answerText(dto.getCorrectAnswers().get(i))
                    .isCaseSensitive(false)
                    .position(i + 1)
                    .build();
            questionAnswerRepository.save(answer);
        }
        
        return convertToDto(question);
    }
    
    public FillInTheBlankQuestionDto update(Integer id, FillInTheBlankQuestionDto dto, Integer adminId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        
        question.setQuestionText(dto.getQuestionText());
        question.setLesson(lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found")));
        question.setQuestionType(questionTypeRepository.findById(dto.getQuestionTypeId())
                .orElseThrow(() -> new RuntimeException("Question type not found")));
        question.setDifficulty(dto.getDifficulty());
        question.setPoints(dto.getPoints());
        question.setUpdatedBy(userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found")));
        question.setUpdatedAt(LocalDateTime.now());
        
        // Delete existing answers
        questionAnswerRepository.deleteByQuestion_Id(id);
        
        // Save new answers
        for (int i = 0; i < dto.getCorrectAnswers().size(); i++) {
            QuestionAnswer answer = QuestionAnswer.builder()
                    .question(question)
                    .answerText(dto.getCorrectAnswers().get(i))
                    .isCaseSensitive(false)
                    .position(i + 1)
                    .build();
            questionAnswerRepository.save(answer);
        }
        
        question = questionRepository.save(question);
        return convertToDto(question);
    }
    
    public void delete(Integer id) {
        questionRepository.deleteById(id);
    }
    
    private FillInTheBlankQuestionDto convertToDto(Question question) {
        List<String> correctAnswers = question.getAnswers().stream()
                .sorted((a, b) -> Integer.compare(a.getPosition(), b.getPosition()))
                .map(QuestionAnswer::getAnswerText)
                .collect(Collectors.toList());
        
        // Parse conversation from question text (assuming it's stored in a specific format)
        // This is a simplified version - in real implementation, you might store this in a separate table
        List<FillInTheBlankQuestionDto.ConversationLineDto> conversation = List.of(
                FillInTheBlankQuestionDto.ConversationLineDto.builder()
                        .content(question.getQuestionText())
                        .speaker("left")
                        .blankPositions(List.of(0)) // Simplified - should be calculated from content
                        .build()
        );
        
        return FillInTheBlankQuestionDto.builder()
                .questionText(question.getQuestionText())
                .lessonId(question.getLesson().getId())
                .questionTypeId(question.getQuestionType().getId())
                .difficulty(question.getDifficulty())
                .points(question.getPoints())
                .conversation(conversation)
                .correctAnswers(correctAnswers)
                .build();
    }
}
