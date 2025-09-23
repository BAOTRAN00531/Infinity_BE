package com.example.infinityweb_be.request;

import com.example.infinityweb_be.domain.dto.enumm.QuestionType;
import com.example.infinityweb_be.domain.dto.question.managment.QuestionOptionDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

@Data
public class QuestionUpsertRequest {
    @NotNull
    private Integer lessonId;          // đổi sang Integer để khớp entity/repo

    // key type để chọn handler: "single_choice", "fill_in_blank", "reorder_words", ...
    @NotNull
    private String type;

    // để map với ManyToOne QuestionType (id) trong entity Question
    @NotNull
    private Integer questionTypeId;

    // id user thực hiện (để set createdBy/updatedBy)
    @NotNull
    private Integer actorUserId;

    @NotNull
    private String questionText;

    private Map<String, Object> payload;     // cho các type phức tạp
    private List<QuestionOptionDto> options; // dùng cho single_choice / single_choice_image

    private String difficulty;  // EASY/MEDIUM/HARD (theo DB của bạn)
    private Integer points;

    // nếu form có các URL media
    private String mediaUrl;
    private String audioUrl;
    private String videoUrl;

    // optional
    private List<String> tags;
}