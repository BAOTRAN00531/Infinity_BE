package com.example.infinityweb_be.service.skill;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final JdbcTemplate jdbc;
    public List<Long> nextQuestions(Long userId, int limit){
        return jdbc.queryForList(
                "SELECT question_id FROM practice_schedule WHERE user_id=? AND next_due <= now() ORDER BY next_due ASC LIMIT ?",
                Long.class, userId, limit);
    }
}
