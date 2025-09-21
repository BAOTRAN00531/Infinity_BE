package com.example.infinityweb_be.domain;

import lombok.Data;

import java.util.List;

@Data
public class SpeakingAssessRequest {
    private String target;        // câu chuẩn (bắt buộc nếu không có questionId)
    private Long questionId;      // nếu có: service sẽ tự lấy correct_text/alt từ DB
    private String lang;          // ví dụ: "en-US" (mặc định en-US)
    private String audioBase64;   // data:audio/wav;base64,AAA... hoặc chỉ chuỗi base64
}
