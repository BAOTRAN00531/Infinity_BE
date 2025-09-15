package com.example.infinityweb_be.security;

import com.example.infinityweb_be.service.UserDetailCustom;
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
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        String registrationId = oauthToken.getAuthorizedClientRegistrationId(); // google hoặc facebook

        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        String picture = null;
        if ("google".equals(registrationId)) {
            // Google trả string
            picture = (String) attributes.get("picture");
        } else if ("facebook".equals(registrationId)) {
            // Facebook trả object
            Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
            Map<String, Object> pictureData = (Map<String, Object>) pictureObj.get("data");
            picture = (String) pictureData.get("url");
        }

        log.info("✅ OAuth2 login thành công [{}]: email={}, name={}, avatar={}",
                registrationId, email, name, picture);
        log.info("OAuth2 Attributes: {}", attributes);

        User savedUser = userService.findOrCreateOAuthUser(email, name, picture);

        UserDetailCustom userDetails = new UserDetailCustom(savedUser);

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
