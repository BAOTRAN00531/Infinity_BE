package com.example.infinityweb_be.domain.dto.question.managment;

import lombok.Data;

@Data
public class QuestionOptionDto {
    private Long id;             // null nếu tạo mới
    private String contentText;  // text hiển thị
    private String imageUrl;     // dùng cho single_choice_image
    private Boolean isCorrect;   // đúng/sai
    private Integer orderIndex;  // thứ tự hiển thị
}