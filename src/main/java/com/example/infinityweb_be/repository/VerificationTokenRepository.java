package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByTokenAndType(String token, String type);

    Optional<VerificationToken> findByUserAndType(User user, String type);
    
    Optional<VerificationToken> findByUserAndTypeAndToken(User user, String type, String token);
    
    void deleteByUserIdAndType(Long userId, String type);
    Optional<VerificationToken> findByUserIdAndType(Long userId, String type);
}
