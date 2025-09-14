package com.example.infinityweb_be.service.skill;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExplainService {
    private final JdbcTemplate jdbc;
    public String explain(Long questionId){
        String gloss = jdbc.queryForObject(
                "SELECT COALESCE(sl.definition, sl.gloss, 'Giải thích đang cập nhật') " +
                        "FROM questions q LEFT JOIN sense_loc sl ON sl.sense_id=q.sense_id AND sl.locale='vi' WHERE q.id=?",
                String.class, questionId);
        return gloss;
    }
}