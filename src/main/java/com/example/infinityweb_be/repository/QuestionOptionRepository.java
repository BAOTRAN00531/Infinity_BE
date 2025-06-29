// QuestionOptionRepository.java
package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Integer> {
    List<QuestionOption> findByQuestionId(Integer questionId);
    void deleteByQuestionId(Integer questionId);

    void deleteByQuestion_Id(Integer questionId);

}