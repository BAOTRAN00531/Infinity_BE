package com.example.infinityweb_be.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Value("${frontend.failure-url}")
    private String frontendFailureUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        log.error("❌ OAuth2 login thất bại: {}", exception.getMessage());

        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendFailureUrl)
                .queryParam("error", URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8))
                .build()
                .toUriString();

        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }
}
