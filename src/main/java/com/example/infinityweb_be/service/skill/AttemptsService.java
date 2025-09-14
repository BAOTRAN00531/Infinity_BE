package com.example.infinityweb_be.service.skill;

import com.example.infinityweb_be.domain.dto.AttemptSubmitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttemptsService {
    private final JdbcTemplate jdbc; // hoặc JPA Repo


    @Transactional
    public void recordAttempt(Long userId, AttemptSubmitDto dto){
        jdbc.update("INSERT INTO quiz_results(user_id, question_id, is_correct, time_spent_ms, created_at) VALUES (?,?,?,?,now())",
                userId, dto.getQuestionId(), dto.isCorrect(), dto.getTimeSpentMs());


        upsertPractice(userId, dto.getQuestionId(), dto.isCorrect());
    }


    private void upsertPractice(Long uid, Long qid, boolean ok){
        PracticeRow r = jdbc.query("SELECT efactor, interval_days, success_streak FROM practice_schedule WHERE user_id=? AND question_id=?",
                rs -> rs.next()? new PracticeRow(rs.getDouble(1), rs.getInt(2), rs.getInt(3)) : null, uid, qid);
        double ef = r==null? 2.5 : r.ef; int streak = r==null? 0 : r.streak; int interval = r==null? 0 : r.interval;


        if(ok){ streak+=1; ef=Math.max(1.3, ef+0.1); interval=(streak<=1?1:(int)Math.round(interval*ef)); }
        else { streak=0; ef=Math.max(1.3, ef-0.2); interval=1; }


        LocalDateTime next = LocalDateTime.now().plusDays(Math.max(1, interval));
        int updated = jdbc.update("UPDATE practice_schedule SET efactor=?, interval_days=?, next_due=?, success_streak=?, last_result=?, last_updated=now() WHERE user_id=? AND question_id=?",
                ef, interval, Timestamp.valueOf(next), streak, ok, uid, qid);


        if(updated==0){
            jdbc.update("INSERT INTO practice_schedule(user_id, question_id, efactor, interval_days, next_due, success_streak, last_result) VALUES (?,?,?,?,?,?,?)",
                    uid, qid, ef, interval, Timestamp.valueOf(next), streak, ok);
        }
    }
    private record PracticeRow(double ef, int interval, int streak){}
}
