package com.example.infinityweb_be.domain.dto.skills;

import lombok.Data;

import java.util.List;

public class WritingDtos {
    @Data
    public static class GradeRequest {
      private Long questionId;     // id câu hỏi (để lấy đáp án CRUD nếu có)
      private String userAnswer;   // bài làm của user
      private String language;
        private List<String> acceptedAnswers;  // các đáp án đúng/ tương đương do FE gửi (có thể rỗng)
        private String rubric;   // "en", "vi", ...
    }
    @Data
    public static class GradeResponse {
      private double score;        // 0..100
      private boolean exactMatch;  // trùng DB hay không
      private boolean semanticallyCorrect;  // đúng về mặt ngữ nghĩa theo đánh giá của AI
      private List<String> reasons;
      private List<String> suggestions;
      private List<String> acceptableParaphrases; // (AI đề xuất)
      private String normalizedUser;              // (chuẩn hóa userAnswer)
    }
  }