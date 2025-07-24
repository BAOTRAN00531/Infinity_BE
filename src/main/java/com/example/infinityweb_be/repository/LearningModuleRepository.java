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
}