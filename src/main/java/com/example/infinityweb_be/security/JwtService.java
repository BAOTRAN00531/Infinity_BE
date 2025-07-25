package com.example.infinityweb_be.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${assigment_java6.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiry;

    @Value("${assigment_java6.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiry;

    /**
     * Getter để controller gọi được thời gian sống của refresh token
     */
    public long getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }

    /**
     * Sinh access token (dựa trên cấu hình @Value)
     */
    public String generateAccessToken(UserDetails userDetails) {
        long minutes = accessTokenExpiry / 60;
        return generateToken(userDetails, minutes, "access");
    }

    /**
     * Sinh refresh token (dựa trên cấu hình @Value)
     */
    public String generateRefreshToken(UserDetails userDetails) {
        long minutes = refreshTokenExpiry / 60;
        return generateToken(userDetails, minutes, "refresh");
    }

    /**
     * Tạo JWT với thời gian sống (phút) và loại token
     */
    private String generateToken(UserDetails userDetails, long expireMinutes, String tokenType) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .issuedAt(now)
                .expiresAt(now.plus(expireMinutes, ChronoUnit.MINUTES))
                .claim("token_type", tokenType)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * Giải mã token và lấy username (subject)
     */
    public String extractUsername(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

    /**
     * Xác minh token khớp với userDetails
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getSubject().equals(userDetails.getUsername());
        } catch (JwtException e) {
            return false;
        }
    }
}
