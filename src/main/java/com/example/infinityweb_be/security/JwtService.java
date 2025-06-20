package com.example.infinityweb_be.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${assigment_java6.jwt.access-token-validity-in-seconds:900}") // mặc định 15 phút
    private long accessTokenExpiry;

    /**
     * -- GETTER --
     *  Lấy thời gian hết hạn của refresh token (giây).
     */
    @Getter
    @Value("${assigment_java6.jwt.refresh-token-validity-in-seconds:604800}") // mặc định 7 ngày
    private long refreshTokenExpiry;

    /**
     * Sinh Access Token cho user.
     */
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessTokenExpiry, "access");
    }

    /**
     * Sinh Refresh Token cho user.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshTokenExpiry, "refresh");
    }

    /**
     * Sinh token chung (access hoặc refresh).
     */
    private String generateToken(UserDetails userDetails, long expirySeconds, String tokenType) {
        try {
            Instant now = Instant.now();
            Instant expiry = now.plus(expirySeconds, ChronoUnit.SECONDS);

            log.info("🔐 Generating {} token for user: {}", tokenType, userDetails.getUsername());

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .subject(userDetails.getUsername())
                    .issuedAt(now)
                    .expiresAt(expiry)
                    .claim("role", userDetails.getAuthorities().stream()
                            .findFirst().map(Object::toString).orElse("ROLE_USER"))
                    .claim("token_type", tokenType)
                    .build();

            String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            log.info("✅ {} token generated successfully", tokenType);
            return token;

        } catch (Exception e) {
            log.error("❌ Failed to generate {} token: {}", tokenType, e.getMessage(), e);
            throw new RuntimeException("Failed to generate JWT", e);
        }
    }

    /**
     * Kiểm tra token có hết hạn không.
     */
    public boolean isTokenExpired(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getExpiresAt().isBefore(Instant.now());
        } catch (Exception e) {
            log.warn("⚠️ Token decode error: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Lấy username từ token.
     */
    public String extractUsername(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

    /**
     * Kiểm tra token có hợp lệ với user không.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            log.error("❌ Token validation failed: {}", e.getMessage());
            return false;
        }
    }

}