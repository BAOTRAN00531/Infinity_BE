package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.Lesson;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    // Lấy lessons theo module.id
    List<Lesson> findByModule_Id(Integer moduleId);

    // Đếm lessons theo module.id
    long countByModule_Id(Integer moduleId);

    @Query("SELECT COALESCE(MAX(l.order), 0) FROM Lesson l WHERE l.module.id = :moduleId")
    Integer findMaxOrderByModule_Id(@Param("moduleId") Integer moduleId);

    // Xoá lessons theo module.id
    @Modifying
    @Transactional
    @Query("DELETE FROM Lesson l WHERE l.module.id = :moduleId")
    void deleteByModule_Id(@Param("moduleId") Integer moduleId);
}