// LessonRepository.java (updated JPQL to use orderIndex)
package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.Lesson;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByModule_Id(Integer moduleId);

    long countByModule_Id(Integer moduleId);

    @Query("SELECT COALESCE(MAX(l.orderIndex), 0) FROM Lesson l WHERE l.module.id = :moduleId")
    Integer findMaxOrderByModule_Id(@Param("moduleId") Integer moduleId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Lesson l WHERE l.module.id = :moduleId")
    void deleteByModule_Id(@Param("moduleId") Integer moduleId);


    // --------- Bổ sung -----------

//    boolean existsByModule_IdAndOrderIndex(Integer moduleId, Integer orderIndex);
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE Lesson l SET l.orderIndex = l.orderIndex + 1 WHERE l.module.id = :moduleId AND l.orderIndex >= :orderIndex")
//    void incrementOrderIndexFrom(@Param("moduleId") Integer moduleId, @Param("orderIndex") Integer orderIndex);



    @Query("SELECT COUNT(l) FROM Lesson l JOIN l.module m WHERE m.course.id = :courseId")
    long countByCourseId(int courseId);


    // ✅ VỊ TRÍ MỚI: Thêm phương thức tìm kiếm IDs vào đây
    @Query("SELECT l.id FROM Lesson l JOIN l.module m WHERE m.course.id = :courseId")
    List<Integer> findLessonIdsByCourseId(@Param("courseId") int courseId);

}
//package com.example.infinityweb_be.repository;
//
//import com.example.infinityweb_be.domain.Lesson;
//import jakarta.transaction.Transactional;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface LessonRepository extends JpaRepository<Lesson, Integer> {
//    // Lấy lessons theo module.id
//    List<Lesson> findByModule_Id(Integer moduleId);
//
//    // Đếm lessons theo module.id
//    long countByModule_Id(Integer moduleId);
//
//    @Query("SELECT COALESCE(MAX(l.order), 0) FROM Lesson l WHERE l.module.id = :moduleId")
//    Integer findMaxOrderByModule_Id(@Param("moduleId") Integer moduleId);
//
//    // Xoá lessons theo module.id
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM Lesson l WHERE l.module.id = :moduleId")
//    void deleteByModule_Id(@Param("moduleId") Integer moduleId);
//}