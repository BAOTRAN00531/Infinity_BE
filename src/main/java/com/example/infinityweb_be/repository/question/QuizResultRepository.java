package com.example.infinityweb_be.repository.question;

import com.example.infinityweb_be.domain.dto.question.student.submit.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    // Có thể thêm các phương thức tìm kiếm tùy chỉnh tại đây nếu cần
}