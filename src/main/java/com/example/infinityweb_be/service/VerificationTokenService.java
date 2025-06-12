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

    // Tạo token xác minh email
    public VerificationToken createVerificationToken(User user) {
        VerificationToken token = VerificationToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .confirmed(false)
                .type("EMAIL_CONFIRMATION")
                .build();
        return tokenRepository.save(token);
    }

    // Tạo refresh token
    public VerificationToken createRefreshToken(User user, long expirySeconds) {
        tokenRepository.findByUserAndType(user, "REFRESH_TOKEN")
                .ifPresent(tokenRepository::delete);

        VerificationToken token = VerificationToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(expirySeconds))
                .confirmed(false)
                .type("REFRESH_TOKEN")
                .build();
        return tokenRepository.save(token);
    }

    public Optional<VerificationToken> validateRefreshToken(String token) {
        return tokenRepository.findByTokenAndType(token, "REFRESH_TOKEN")
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    public void deleteRefreshToken(User user) {
        tokenRepository.findByUserAndType(user, "REFRESH_TOKEN")
                .ifPresent(tokenRepository::delete);
    }
    public VerificationToken saveToken(User user, String tokenValue, String type) {
        VerificationToken token = new VerificationToken();
        token.setUser(user);
        token.setToken(tokenValue);
        token.setConfirmed(false);
        token.setType(type); // "refresh" hoặc "verify"
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(type.equals("refresh") ? 10080 : 10)); // refresh: 7 ngày, verify: 10 phút

        return tokenRepository.save(token);
    }

}
