package com.example.infinityweb_be.repository.progress;

import com.example.infinityweb_be.domain.progress.UserProgress;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUserId(Integer userId);

    // Sửa lại phương thức này với @Query rõ ràng
    @Query("SELECT COUNT(up) FROM UserProgress up " +
            "WHERE up.user.id = :userId " +
            "AND up.entityType = 'lesson' " +
            "AND up.progressPercentage = 100.0 " + // Sửa thành số nguyên để phù hợp với BigDecimal
            "AND up.entityId IN :lessonIds")
    long countCompletedLessons(@Param("userId") int userId,
                               @Param("lessonIds") List<Integer> lessonIds);
}