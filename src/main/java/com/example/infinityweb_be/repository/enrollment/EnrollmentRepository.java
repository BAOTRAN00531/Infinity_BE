//
// src/main/java/com/example/infinityweb_be/repository/EnrollmentRepository.java
package com.example.infinityweb_be.repository.enrollment;


import com.example.infinityweb_be.domain.enrollment.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    // ✅ Kiểm tra xem người dùng đã đăng ký khóa học chưa
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END FROM Enrollment e WHERE e.user.id = ?1 AND e.course.id = ?2")
    boolean existsByUserIdAndCourseId(Integer userId, Integer courseId);

    // ✅ Tìm kiếm đăng ký theo userId và courseId
    Optional<Enrollment> findByUserIdAndCourseId(Integer userId, Integer courseId);
}