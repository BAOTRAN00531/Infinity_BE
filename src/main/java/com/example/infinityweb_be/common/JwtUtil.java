package com.example.infinityweb_be.common;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static JwtDecoder jwtDecoder;

    @Autowired
    public JwtUtil(JwtDecoder decoder) {
        JwtUtil.jwtDecoder = decoder;
    }

    public static String extractUserId(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        Object userId = jwt.getClaim("userId");
        if (userId == null) {
            throw new RuntimeException("Không tìm thấy userId trong token");
        }
        return userId.toString();
    }
}