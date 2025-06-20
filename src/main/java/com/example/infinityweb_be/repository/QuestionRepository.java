package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByLessonId(Integer lessonId);
}
