package com.example.infinityweb_be.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        org.springframework.security.core.Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

        String email = (String) attributes.get("email");


        log.info("✅ OAuth2 login thành công: {}", email);


        // tạo UserDetails giả để sinh JWT
        User userDetails = new User(
                email,
                "", // password không cần thiết
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String jwt = jwtService.generateAccessToken(userDetails);
        log.info("JWT được sinh ra: {}", jwt);

        String header = request.getHeader("Authorization");
        log.info("Authorization header: {}", header);


        String redirectUrl = "http://localhost:3000/oauth2/success?token=" + jwt;

        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }
}
