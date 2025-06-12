package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.VerificationToken;
import com.example.infinityweb_be.domain.dto.LoginDTO;
import com.example.infinityweb_be.domain.dto.RegisterDTO;
import com.example.infinityweb_be.domain.dto.ResLoginDTO;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.repository.VerificationTokenRepository;
import com.example.infinityweb_be.security.JwtService;
import com.example.infinityweb_be.service.UserDetailCustom;
import com.example.infinityweb_be.service.UserService;
import com.example.infinityweb_be.service.VerificationTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final VerificationTokenService verificationTokenService;

    @Value("${assigment_java6.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiry;

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

        // 1. Xác thực
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),      // có thể là username hoặc email
                        loginDTO.getPassword())
        );

        // 2. Lưu vào context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Lấy UserDetails từ Authentication (KHÔNG cast entity!)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 4. Lấy entity User để trả về client (nếu cần)
        User user = userService.findByEmailOrUsername(loginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 5. Sinh JWT
        String accessToken  = jwtService.generateAccessToken(userDetails);
        String refreshToken = verificationTokenService
                .createRefreshToken(user, refreshTokenExpiry)
                .getToken();

        // 6. Build response
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                user.getId(), user.getEmail(), user.getUsername());

        ResLoginDTO resp = new ResLoginDTO();
        resp.setAccess_token(accessToken);
        resp.setUserp(userLogin);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true).secure(true).path("/").maxAge(refreshTokenExpiry).build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(resp);
    }


    @GetMapping("/refresh-token")
    public ResponseEntity<ResLoginDTO> refreshToken(@CookieValue("refresh_token") String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var verificationTokenOpt = verificationTokenService.validateRefreshToken(token);
        if (verificationTokenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        VerificationToken verificationToken = verificationTokenOpt.get();
        if (verificationToken.isConfirmed() && verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = verificationToken.getUser();
        if (!user.isActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // User chưa active
        }

        String newAccessToken = jwtService.generateAccessToken((UserDetails) user);
        String newRefreshToken = verificationTokenService.createRefreshToken(user, refreshTokenExpiry).getToken();

        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getEmail(), user.getUsername());
        ResLoginDTO response = new ResLoginDTO();
        response.setAccess_token(newAccessToken);
        response.setUserp(userLogin);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiry)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refresh_token", required = false) String token) {
        if (token != null) {
            verificationTokenService.validateRefreshToken(token)
                    .ifPresent(t -> verificationTokenService.deleteRefreshToken(t.getUser()));
        }

        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        try {
            // 1. Tạo user mới
            User newUser = userService.registerNewUser(registerDTO); // đã mã hóa password, set role, gửi email xác thực

            // 2. Tạo access token
            UserDetails userDetails = new UserDetailCustom(newUser);
            String accessToken = jwtService.generateAccessToken(userDetails);

            // 3. Tạo refresh token và lưu vào bảng verification_token (type=refresh)
            String refreshToken = jwtService.generateRefreshToken(userDetails);
            verificationTokenService.saveToken(newUser, refreshToken, "refresh");

            // 4. Tạo response DTO
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    newUser.getId(),
                    newUser.getEmail(),
                    newUser.getUsername()
            );

            ResLoginDTO response = new ResLoginDTO();
            response.setAccess_token(accessToken);
            response.setUserp(userLogin);

            // 5. Đặt cookie refresh_token
            ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7 ngày
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(response);

        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đăng ký thất bại");
        }
    }
    // com.example.infinityweb_be.controller/AuthenticationController.java
    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestParam("token") String token) {
        log.info("Starting verification process for token: {}", token);
        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByTokenAndType(token, "EMAIL_CONFIRMATION");

        if (optionalToken.isEmpty()) {
            log.error("No matching token found in database for token: {}", token);
            return ResponseEntity.badRequest().body(Map.of("message", "Token không hợp lệ hoặc đã bị sử dụng"));
        }

        VerificationToken vt = optionalToken.get();
        log.info("Token details - expiresAt: {}, confirmed: {}, user: {}", vt.getExpiresAt(), vt.isConfirmed(), vt.getUser().getEmail());

        if (vt.isConfirmed()) {
            log.warn("Token already confirmed for user: {}", vt.getUser().getEmail());
            return ResponseEntity.badRequest().body(Map.of("message", "Token đã được xác thực trước đó"));
        }

        if (vt.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.error("Token expired - user: {}, expiresAt: {}", vt.getUser().getEmail(), vt.getExpiresAt());
            return ResponseEntity.badRequest().body(Map.of("message", "Token xác thực đã hết hạn"));
        }

        User user = vt.getUser();
        if (!user.isActive()) {
            user.setActive(true);
            try {
                User savedUser = userRepository.save(user);
                log.info("User activated successfully - email: {}, id: {}", savedUser.getEmail(), savedUser.getId());
            } catch (Exception e) {
                log.error("Failed to save user activation - email: {}, error: {}", user.getEmail(), e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Lỗi khi kích hoạt tài khoản: " + e.getMessage()));
            }
        } else {
            log.info("User already active - email: {}", user.getEmail());
        }

        vt.setConfirmed(true);
        verificationTokenRepository.save(vt);
        log.info("Token marked as confirmed - user: {}", user.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:3001/verify-success"));
        return new ResponseEntity<>(Map.of("message", "Xác thực email thành công. Chuyển hướng sau 3 giây..."), headers, HttpStatus.SEE_OTHER);
    }
}
