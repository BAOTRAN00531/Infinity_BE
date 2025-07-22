package com.example.infinityweb_be.security;

import com.example.infinityweb_be.service.UserService;
import com.example.infinityweb_be.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Value("${frontend.success-url}")
    private String frontendSuccessUrl;

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        log.info("✅ OAuth2 login thành công: email={}, name={}, avatar={}", email, name, picture);
        log.info("OAuth2 Attributes: {}", attributes);

        // 👇 lưu user vào DB nếu chưa tồn tại
        User savedUser = userService.findOrCreateOAuthUser(email, name, picture);

        // 👇 dùng savedUser để tạo UserDetails (hoặc bạn có thể tự implement UserDetails)
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        savedUser.getEmail(),
                        "",
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        String jwt = jwtService.generateAccessToken(userDetails);

        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendSuccessUrl)
                .queryParam("token", jwt)
                .queryParam("name", URLEncoder.encode(name, StandardCharsets.UTF_8))
                .queryParam("avatar", URLEncoder.encode(picture, StandardCharsets.UTF_8))
                .build()
                .toUriString();

        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }


}
