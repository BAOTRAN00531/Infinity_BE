package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.LearningModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface LearningModuleRepository extends JpaRepository<LearningModule, Integer> {
    List<LearningModule> findByCourseId(Integer courseId);


    @Query("SELECT MAX(m.order) FROM LearningModule m WHERE m.course.id = :courseId")
    Integer findMaxOrderByCourseId(@Param("courseId") Integer courseId);

    @Query("UPDATE LearningModule m SET m.status = :status WHERE m.course.id = :courseId")
    @org.springframework.data.jpa.repository.Modifying
    int updateStatusByCourseId(@Param("courseId") Integer courseId, @Param("status") String status);

    @Query(value = """
            SELECT TOP 1 m.id
            FROM Modules m
                     INNER JOIN Lessons l ON m.id = l.module_id
                     INNER JOIN Questions q ON l.id = q.lesson_id
                     LEFT JOIN user_question_progress up
                               ON q.id = up.question_id
                                   AND up.user_id = :userId
            WHERE EXISTS (SELECT 1
                          FROM user_question_progress up1
                                   INNER JOIN Questions q1 ON up1.question_id = q1.id
                                   INNER JOIN Lessons l1 ON q1.lesson_id = l1.id
                          WHERE l1.module_id = m.id
                            AND up1.user_id = :userId
                            AND up1.is_completed = 1)
              AND EXISTS (SELECT 1
                          FROM Questions q2
                                   LEFT JOIN user_question_progress up2
                                             ON q2.id = up2.question_id
                                                 AND up2.user_id = :userId
                                   INNER JOIN Lessons l2 ON q2.lesson_id = l2.id
                          WHERE l2.module_id = m.id
                            AND (up2.is_completed = 0 OR up2.is_completed IS NULL))
              AND m.course_id = :courseId
            ORDER BY m.[order]
            """, nativeQuery = true)
    Integer getInProgressModuleId(@Param("courseId") Integer courseId,
                                  @Param("userId") Integer userId);
}