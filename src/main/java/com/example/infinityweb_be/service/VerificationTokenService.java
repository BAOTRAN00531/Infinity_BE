package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.VerificationToken;
import com.example.infinityweb_be.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository tokenRepository;

    public VerificationToken createVerificationToken(User user) {
        VerificationToken token = VerificationToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .confirmed(false)
                .build();
        return tokenRepository.save(token);
    }

    public Optional<VerificationToken> validateToken(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> !t.isConfirmed())
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    public void confirmToken(VerificationToken token) {
        token.setConfirmed(true);
        tokenRepository.save(token);
    }
}
