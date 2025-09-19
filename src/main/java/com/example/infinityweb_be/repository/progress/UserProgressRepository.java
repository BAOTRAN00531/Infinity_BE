package com.example.infinityweb_be.repository.progress;

// src/main/java/com/example/infinityweb_be/repository/progress/UserProgressRepository.java

import com.example.infinityweb_be.domain.progress.UserProgress;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUserId(Integer userId);

    @Query("SELECT COUNT(up) FROM UserProgress up " +
            "WHERE up.user.id = :userId " +
            "AND up.entityType = 'lesson' " +
            "AND up.progressPercentage = 100.0 " +
            "AND up.entityId IN :lessonIds")
    long countCompletedLessons(@Param("userId") Integer userId,
                               @Param("lessonIds") List<Integer> lessonIds);

    @Query("""
        SELECT COUNT(m)
        FROM LearningModule m
        WHERE m.course.id = :courseId AND (
            SELECT COUNT(l) FROM Lesson l WHERE l.module.id = m.id
        ) = (
            SELECT COUNT(up) FROM UserProgress up
            WHERE up.user.id = :userId
            AND up.entityType = 'lesson'
            AND up.progressPercentage = 100.0
            AND up.entityId IN (SELECT l2.id FROM Lesson l2 WHERE l2.module.id = m.id)
        )
    """)
    Long countCompletedModulesInCourse(@Param("userId") Integer userId, @Param("courseId") Integer courseId);


    // ✅ Phương thức mới: Đếm số bài học đã hoàn thành của một user trong một module cụ thể
    @Query("SELECT COUNT(up) FROM UserProgress up " +
            "WHERE up.user.id = :userId " +
            "AND up.entityType = 'lesson' " +
            "AND up.progressPercentage = 100.0 " +
            "AND up.entityId IN (SELECT l.id FROM Lesson l WHERE l.module.id = :moduleId)")
    Long countCompletedLessonsInModule(@Param("userId") Integer userId, @Param("moduleId") Integer moduleId);


    @Query("SELECT up.entityId FROM UserProgress up " +
            "WHERE up.user.id = :userId " +
            "AND up.entityType = 'lesson' " +
            "AND up.progressPercentage = 100.0 " +
            "AND up.entityId IN :lessonIds")
    List<Integer> findCompletedLessonIds(@Param("userId") Integer userId,
                                         @Param("lessonIds") List<Integer> lessonIds);

    Optional<UserProgress> findByUserIdAndEntityIdAndEntityType(Integer userId, Integer entityId, String entityType);

    default Optional<UserProgress> findByUserIdAndLessonId(Integer userId, Integer lessonId) {
        return findByUserIdAndEntityIdAndEntityType(userId, lessonId, "lesson");
    }


}