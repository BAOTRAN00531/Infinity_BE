package com.example.infinityweb_be.repository.question;

import com.example.infinityweb_be.domain.dto.question.student.UserQuestionProgressDto;
import com.example.infinityweb_be.entity.UserQuestionProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQuestionProgressRepository extends JpaRepository<UserQuestionProgressEntity, Long> {


    @Query(value = """
            SELECT q.lesson_id                                 AS lessonId,
                   COUNT(*)                                    AS questionCount,
                   SUM(IIF(uqp.question_id IS NOT NULL, 1, 0)) AS completedCount
            FROM Questions AS q
                     LEFT JOIN user_question_progress uqp
                               ON q.lesson_id = uqp.lesson_id
                                   AND q.id = uqp.question_id
                                   AND uqp.is_completed = 1
                                   AND uqp.user_id = :userId
            WHERE q.lesson_id IN :lessonIds
            GROUP BY q.lesson_id
            """, nativeQuery = true)
    List<UserQuestionProgressDto> getUserQuestionProgress(Integer userId, List<Integer> lessonIds);
}
