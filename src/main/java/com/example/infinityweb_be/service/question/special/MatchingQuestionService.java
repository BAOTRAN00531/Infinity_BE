package com.example.infinityweb_be.service.question.special;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionOption;
import com.example.infinityweb_be.domain.dto.question.special.MatchingQuestionDto;
import com.example.infinityweb_be.repository.question.QuestionRepository;
import com.example.infinityweb_be.repository.question.QuestionOptionRepository;
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
public class MatchingQuestionService {
    
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    
    public List<MatchingQuestionDto> getAll() {
        return questionRepository.findByQuestionType_Id(7) // Matching type
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public MatchingQuestionDto getById(Integer id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        return convertToDto(question);
    }
    
    public List<MatchingQuestionDto> getByLessonId(Integer lessonId) {
        return questionRepository.findByLesson_IdAndQuestionType_Id(lessonId, 7)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public MatchingQuestionDto create(MatchingQuestionDto dto, Integer adminId) {
        System.out.println("Creating matching question with dto: " + dto);
        System.out.println("Lesson ID: " + dto.getLessonId());
        System.out.println("Question Type ID: " + dto.getQuestionTypeId());
        System.out.println("Admin ID: " + adminId);
        
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
        
        // Save pairs as options
        for (int i = 0; i < dto.getPairs().size(); i++) {
            MatchingQuestionDto.MatchingPairDto pair = dto.getPairs().get(i);
            QuestionOption option = QuestionOption.builder()
                    .question(question)
                    .optionText(pair.getLeftItem() + " → " + pair.getRightItem())
                    .isCorrect(true) // All pairs are correct matches
                    .position(i + 1)
                    .build();
            questionOptionRepository.save(option);
        }
        
        return convertToDto(question);
    }
    
    public MatchingQuestionDto update(Integer id, MatchingQuestionDto dto, Integer adminId) {
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
        
        // Delete existing options
        questionOptionRepository.deleteByQuestion_Id(id);
        
        // Save new pairs as options
        for (int i = 0; i < dto.getPairs().size(); i++) {
            MatchingQuestionDto.MatchingPairDto pair = dto.getPairs().get(i);
            QuestionOption option = QuestionOption.builder()
                    .question(question)
                    .optionText(pair.getLeftItem() + " → " + pair.getRightItem())
                    .isCorrect(true)
                    .position(i + 1)
                    .build();
            questionOptionRepository.save(option);
        }
        
        question = questionRepository.save(question);
        return convertToDto(question);
    }
    
    public void delete(Integer id) {
        questionRepository.deleteById(id);
    }
    
    private MatchingQuestionDto convertToDto(Question question) {
        List<MatchingQuestionDto.MatchingPairDto> pairs = question.getOptions().stream()
                .sorted((a, b) -> Integer.compare(a.getPosition(), b.getPosition()))
                .map(option -> {
                    String[] parts = option.getOptionText().split(" → ");
                    return MatchingQuestionDto.MatchingPairDto.builder()
                            .leftItem(parts[0])
                            .rightItem(parts.length > 1 ? parts[1] : "")
                            .build();
                })
                .collect(Collectors.toList());
        
        return MatchingQuestionDto.builder()
                .questionText(question.getQuestionText())
                .lessonId(question.getLesson().getId())
                .questionTypeId(question.getQuestionType().getId())
                .difficulty(question.getDifficulty())
                .points(question.getPoints())
                .instructions("Match the items in the left column with the items in the right column")
                .pairs(pairs)
                .shuffleOptions(false) // Default value
                .build();
    }
}
