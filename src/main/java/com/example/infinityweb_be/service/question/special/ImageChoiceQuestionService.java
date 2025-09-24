package com.example.infinityweb_be.service.question.special;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.QuestionOption;
import com.example.infinityweb_be.domain.dto.question.special.ImageChoiceQuestionDto;
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
public class ImageChoiceQuestionService {
    
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    
    public List<ImageChoiceQuestionDto> getAll() {
        return questionRepository.findByQuestionType_Id(9) // Image Choice type
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public ImageChoiceQuestionDto getById(Integer id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        return convertToDto(question);
    }
    
    public List<ImageChoiceQuestionDto> getByLessonId(Integer lessonId) {
        return questionRepository.findByLesson_IdAndQuestionType_Id(lessonId, 9)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public ImageChoiceQuestionDto create(ImageChoiceQuestionDto dto, Integer adminId) {
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
        
        // Save options with images
        for (int i = 0; i < dto.getOptions().size(); i++) {
            ImageChoiceQuestionDto.ImageOptionDto optionDto = dto.getOptions().get(i);
            QuestionOption option = QuestionOption.builder()
                    .question(question)
                    .optionText(optionDto.getOptionText())
                    .isCorrect(optionDto.getCorrect())
                    .position(optionDto.getPosition() != null ? optionDto.getPosition() : i + 1)
                    .imageUrl(optionDto.getImageUrl())
                    .build();
            questionOptionRepository.save(option);
        }
        
        return convertToDto(question);
    }
    
    public ImageChoiceQuestionDto update(Integer id, ImageChoiceQuestionDto dto, Integer adminId) {
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
        
        // Save new options
        for (int i = 0; i < dto.getOptions().size(); i++) {
            ImageChoiceQuestionDto.ImageOptionDto optionDto = dto.getOptions().get(i);
            QuestionOption option = QuestionOption.builder()
                    .question(question)
                    .optionText(optionDto.getOptionText())
                    .isCorrect(optionDto.getCorrect())
                    .position(optionDto.getPosition() != null ? optionDto.getPosition() : i + 1)
                    .imageUrl(optionDto.getImageUrl())
                    .build();
            questionOptionRepository.save(option);
        }
        
        question = questionRepository.save(question);
        return convertToDto(question);
    }
    
    public void delete(Integer id) {
        questionRepository.deleteById(id);
    }
    
    private ImageChoiceQuestionDto convertToDto(Question question) {
        List<ImageChoiceQuestionDto.ImageOptionDto> options = question.getOptions().stream()
                .sorted((a, b) -> Integer.compare(a.getPosition(), b.getPosition()))
                        .map(option -> ImageChoiceQuestionDto.ImageOptionDto.builder()
                        .optionText(option.getOptionText())
                        .imageUrl(option.getImageUrl())
                        .correct(option.isCorrect())
                        .position(option.getPosition())
                        .build())
                .collect(Collectors.toList());
        
        return ImageChoiceQuestionDto.builder()
                .questionText(question.getQuestionText())
                .lessonId(question.getLesson().getId())
                .questionTypeId(question.getQuestionType().getId())
                .difficulty(question.getDifficulty())
                .points(question.getPoints())
                .options(options)
                .build();
    }
}
