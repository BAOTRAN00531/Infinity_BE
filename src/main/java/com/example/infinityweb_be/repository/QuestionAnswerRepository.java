package com.example.infinityweb_be.repository;


import com.example.infinityweb_be.domain.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Integer> {
    List<QuestionAnswer> findByQuestionId(Integer questionId);
}