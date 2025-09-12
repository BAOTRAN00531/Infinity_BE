package com.example.infinityweb_be.common; // hoặc .security nếu bạn để trong security

import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthHelper {

    private final UserRepository userRepository;

    public int getCurrentUserId(JwtAuthenticationToken token) {
        return getCurrentUser(token).getId();
    }

    public User getCurrentUser(JwtAuthenticationToken token) {
        String email = token.getName(); // Spring Security sẽ set email vào name
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}
