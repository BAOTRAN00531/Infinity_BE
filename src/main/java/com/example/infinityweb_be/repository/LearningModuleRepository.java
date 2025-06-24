package com.example.infinityweb_be.repository;
import com.example.infinityweb_be.domain.LearningModule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LearningModuleRepository extends JpaRepository<LearningModule, Integer> {
    List<LearningModule> findByCourseId(Integer courseId);

}