package com.example.infinityweb_be.repository.question;

import com.example.infinityweb_be.domain.dto.question.student.UserLessonQuestionProgressDto;
import com.example.infinityweb_be.domain.dto.question.student.UserModuleQuestionProgressDto;
import com.example.infinityweb_be.entity.UserQuestionProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuestionProgressRepository extends JpaRepository<UserQuestionProgressEntity, Long> {

    Optional<UserQuestionProgressEntity> findByUser_IdAndQuestion_Id(Integer userId, Integer questionId);

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
    List<UserLessonQuestionProgressDto> getUserQuestionProgress(Integer userId, List<Integer> lessonIds);

    List<UserQuestionProgressEntity> findByUser_IdAndLesson_IdAndIsCompleted(Integer userId, Integer lessonId, boolean isCompleted);

    @Query(value = """
            SELECT m.id AS moduleId,
                   COUNT(q.id) AS questionCount,
                   SUM(CASE WHEN uqp.is_completed = 1 THEN 1 ELSE 0 END) AS completedCount
            FROM Modules m
            LEFT JOIN Lessons l ON l.module_id = m.id
            LEFT JOIN Questions q ON q.lesson_id = l.id
            LEFT JOIN user_question_progress uqp ON uqp.question_id = q.id AND uqp.user_id = :userId
            WHERE m.id IN :moduleIds
            GROUP BY m.id
            """, nativeQuery = true)
    List<UserModuleQuestionProgressDto> getModuleProgressByUser(Integer userId, List<Integer> moduleIds);
}
