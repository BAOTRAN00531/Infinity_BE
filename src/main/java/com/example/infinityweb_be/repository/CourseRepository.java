package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.course.Course;
import com.example.infinityweb_be.domain.dto.student.StudentCourseProgressDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByLanguage_Code(String code);

    List<Course> findByLanguageId(Integer languageId);

    @Query("""
    SELECT new com.example.infinityweb_be.domain.dto.student.StudentCourseProgressDto(
        c.id,
        c.name,
        c.thumbnail,
        c.price,
        (SELECT COUNT(m) FROM LearningModule m WHERE m.course.id = c.id),
        CAST(0 AS long),
        CASE
            WHEN (SELECT COUNT(l) FROM Lesson l JOIN l.module m WHERE m.course.id = c.id) = 0 THEN 0.0
            ELSE (CAST(COUNT(up.id) AS double) / (SELECT COUNT(l2) FROM Lesson l2 JOIN l2.module m2 WHERE m2.course.id = c.id)) * 100.0
        END
    )
    FROM Enrollment e
    JOIN e.course c
    LEFT JOIN c.modules m
    LEFT JOIN m.lessons l
    LEFT JOIN UserProgress up ON 
        up.entityType = 'lesson' AND 
        up.entityId = l.id AND 
        up.user.id = :userId AND 
        up.progressPercentage = 100.0
    WHERE e.user.id = :userId
    GROUP BY c.id, c.name, c.thumbnail, c.price
    """)
    List<StudentCourseProgressDto> findStudentDashboardCourses(@Param("userId") Integer userId);



    // chạy count course trong page language
//    Long countByLanguageId(Integer languageId);
}
