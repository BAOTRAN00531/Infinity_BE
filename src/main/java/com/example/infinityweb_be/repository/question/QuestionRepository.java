package com.example.infinityweb_be.repository.question;

import com.example.infinityweb_be.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByLesson_Id(Integer lessonId);

    void deleteByLesson_Id(int id);
}
