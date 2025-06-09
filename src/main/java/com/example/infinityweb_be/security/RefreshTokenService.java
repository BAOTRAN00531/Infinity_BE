package com.example.infinityweb_be.security;

import com.example.infinityweb_be.domain.RefreshToken;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.repository.RefreshTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${assigment_java6.jwt.refresh-token-validity-in-seconds:604800}") // default: 7 days
    private long refreshTokenExpirySeconds;

    /**
     * Sinh Refresh Token và lưu vào DB.
     */
    public RefreshToken createRefreshToken(User user) {
        // Xoá token cũ (nếu chỉ cho phép 1 token/user)
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpirySeconds))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Kiểm tra token có hợp lệ không.
     */
    public Optional<RefreshToken> validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(rt -> rt.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    /**
     * Xoá token theo user (ví dụ khi logout).
     */
    @Transactional
    public void deleteTokenByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
