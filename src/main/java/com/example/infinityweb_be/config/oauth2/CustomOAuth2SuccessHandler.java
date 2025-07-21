//package com.example.infinityweb_be.config.oauth2;
//
//import com.example.infinityweb_be.security.JwtService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//@Component
//@RequiredArgsConstructor
//public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
//
//    private final JwtService jwtService;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//
//        String email = (String) oauthToken.getPrincipal().getAttributes().get("email");
//
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
//                email, "", List.of(() -> "ROLE_USER"));
//
//        String jwt = jwtService.generateAccessToken(userDetails);
//
//        String redirectUrl = "http://localhost:3000/oauth2/success?token=" + jwt;
//        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
//    }
//}
