//package com.example.infinityweb_be.repository.enrollment;
//
//import com.example.infinityweb_be.domain.dto.student.StudentCourseProgressDto;
//import com.example.infinityweb_be.domain.enrollment.Enrollment;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
//
//    boolean existsByUserIdAndCourseId(Integer userId, Integer courseId);
//
//    @Query("""
//    SELECT new com.example.infinityweb_be.domain.dto.student.StudentCourseProgressDto(
//        c.id,
//        c.name,
//        c.thumbnail,
//        c.price,
//        COUNT(DISTINCT m.id),
//        COUNT(DISTINCT up),
//        CASE
//            WHEN COUNT(DISTINCT m.id) = 0 THEN 0.0
//            ELSE (COUNT(DISTINCT up) * 1.0 / COUNT(DISTINCT m.id)) * 100.0
//        END
//    )
//    FROM Enrollment e
//    JOIN e.course c
//    LEFT JOIN c.modules m
//    LEFT JOIN UserProgress up ON
//        up.entityType = 'module' AND
//        up.entityId = m.id AND
//        up.user.id = :userId AND
//        up.progressPercentage = 100.0
//    WHERE e.userId = :userId
//    GROUP BY c.id, c.name, c.thumbnail, c.price
//""")
//    List<StudentCourseProgressDto> findStudentDashboardCourses(@Param("userId") Integer userId);
//
//}
