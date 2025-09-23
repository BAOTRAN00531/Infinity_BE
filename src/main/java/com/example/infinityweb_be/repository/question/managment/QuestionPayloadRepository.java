package com.example.infinityweb_be.repository.question.managment;

import com.example.infinityweb_be.domain.QuestionPayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionPayloadRepository extends JpaRepository<QuestionPayload, Integer> {
    Optional<QuestionPayload> findByQuestionId(Integer questionId);
}

