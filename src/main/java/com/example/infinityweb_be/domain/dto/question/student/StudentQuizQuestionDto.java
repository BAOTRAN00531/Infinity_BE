// StudentQuizQuestionDto.java
package com.example.infinityweb_be.domain.dto.question.student;


import com.example.infinityweb_be.domain.dto.question.admin.MediaDto;
import lombok.Data;
import java.util.List;

@Data
public class StudentQuizQuestionDto {
    private Integer id;
    private String questionText;
    private String type; // ✅ THÊM TRƯỜNG NÀY - code từ QuestionType (ví dụ: "multiple_choice", "essay")
    private String difficulty;
    private Integer points;
    private MediaDto media;
    private List<StudentQuizOptionDto> options;
    private List<StudentQuizAnswerDto> answers;
}