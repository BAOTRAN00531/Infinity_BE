//package com.example.infinityweb_be.repository.student;
//
//import com.example.infinityweb_be.domain.dto.student.StudentCourseProgressDto;
//import com.example.infinityweb_be.domain.enrollment.Enrollment;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface StudentDashboardRepository extends JpaRepository<Enrollment, Integer> {
//
//    @Query("""
//        SELECT new com.example.infinityweb_be.domain.dto.StudentCourseProgressDto(
//            c.id,
//            c.name,
//            c.thumbnail,
//            c.price,
//            COUNT(m.id),
//            COUNT(CASE WHEN um.completed = true THEN 1 END),
//            COALESCE(ROUND(
//                COUNT(CASE WHEN um.completed = true THEN 1 END) * 100.0 / NULLIF(COUNT(m.id), 0), 2
//            ), 0)
//        )
//        FROM Enrollment e
//        JOIN e.course c
//        LEFT JOIN Module m ON m.course.id = c.id
//        LEFT JOIN UserModuleProgress um ON um.module.id = m.id AND um.user.id = e.user.id
//        WHERE e.user.id = :userId
//        GROUP BY c.id, c.name, c.thumbnail, c.price
//    """)
//    List<StudentCourseProgressDto> findDashboardCourses(@Param("userId") Integer userId);
//}
