// QuestionTypeRepository.java
package com.example.infinityweb_be.repository.question;

import com.example.infinityweb_be.domain.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionType, Integer> {
    Optional<QuestionType> findByCode(String code);
}
