package com.example.infinityweb_be.repository.progress;

import com.example.infinityweb_be.domain.progress.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUserId(Integer userId);
}
