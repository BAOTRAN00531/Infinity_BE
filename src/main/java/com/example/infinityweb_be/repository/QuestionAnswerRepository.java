// QuestionAnswerRepository.java
package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Integer> {
    List<QuestionAnswer> findByQuestionId(Integer questionId);
    void deleteByQuestionId(Integer questionId);

    void deleteByQuestion_Id(Integer questionId);

}