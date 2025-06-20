package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByModuleId(Integer moduleId);
}
