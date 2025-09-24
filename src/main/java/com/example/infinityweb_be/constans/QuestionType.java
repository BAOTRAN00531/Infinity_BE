package com.example.infinityweb_be.constans;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum QuestionType {

    MULTIPLE_CHOICE_SINGLE("multiple_choice_single"),
    MULTIPLE_SINGLE_MULTI("multiple_choice_multi"),
    TEXT_INPUT("text_input"),
    FILL_IN_THE_BLANK("fill_in_the_blank"),
    MATCHING("matching");

    private final String code;
    private static final Map<String, QuestionType> map = new HashMap<>();

    static {
        for (QuestionType questionType : QuestionType.values()) {
            map.put(questionType.getCode(), questionType);
        }
    }

    public static QuestionType getByCode(String code) {
        if (!map.containsKey(code)) {
            throw new IllegalArgumentException("Question type " + code + " not found.");
        }
        return map.get(code);
    }
}
