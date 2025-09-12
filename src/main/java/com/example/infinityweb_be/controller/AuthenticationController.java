package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.CustomExceptionResponse;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.VerificationToken;
import com.example.infinityweb_be.domain.dto.ForgotPasswordDTO;
import com.example.infinityweb_be.domain.dto.LoginDTO;
import com.example.infinityweb_be.domain.dto.RegisterDTO;
import com.example.infinityweb_be.domain.dto.ResLoginDTO;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.repository.VerificationTokenRepository;
import com.example.infinityweb_be.security.JwtConfig;
import com.example.infinityweb_be.security.JwtService;
import com.example.infinityweb_be.service.UserDetailCustom;
import com.example.infinityweb_be.service.UserService;
import com.example.infinityweb_be.service.VerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

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
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    @Value("${assigment_java6.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiry;
    private final KeyPair keyPair;
    private final JwtConfig jwtConfig;

    @GetMapping("/public-key")
    public Map<String, String> getPublicKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder().encodeToString(publicKey.getEncoded()) +
                "\n-----END PUBLIC KEY-----";
        return Map.of("publicKey", publicKeyPem);
    }


    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        // 1. Xác thực
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        // 2. Lưu vào context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Lấy UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 4. Lấy User entity
        User user = userService.findByEmailOrUsername(loginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 5. Sinh JWT
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = verificationTokenService.createRefreshToken(user, jwtService.getRefreshTokenExpiry()).getToken();

        // 6. Build response
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getEmail(), user.getUsername());
        ResLoginDTO resp = new ResLoginDTO();
        resp.setAccess_token(accessToken);
        resp.setUserp(userLogin);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true).secure(true).path("/").maxAge(jwtService.getRefreshTokenExpiry()).build();

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

        String newAccessToken = jwtService.generateAccessToken(new UserDetailCustom(user));
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
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO,
                                      HttpServletRequest request) {
        try {
            // 1. Tạo user mới
            User newUser = userService.registerNewUser(registerDTO);

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
            CustomExceptionResponse<Object> error = new CustomExceptionResponse<>();
            error.setStatusCode(HttpStatus.BAD_REQUEST);
            error.setMessage(ex.getMessage());
            error.setData(null);
            error.setPath(request.getRequestURI());
            error.setTimestamp(LocalDateTime.now());
            return ResponseEntity.badRequest().body(error);

        } catch (Exception e) {
            CustomExceptionResponse<Object> error = new CustomExceptionResponse<>();
            error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            error.setMessage("Đăng ký thất bại");
            error.setData(null);
            error.setPath(request.getRequestURI());
            error.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    // com.example.infinityweb_be.controller/AuthenticationController.java
    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestParam("token") String token) {
        log.info("Starting verification process for token: {}", token);

        Optional<VerificationToken> optionalToken =
                verificationTokenRepository.findByTokenAndType(token, "EMAIL_CONFIRMATION");

        if (optionalToken.isEmpty()) {
            log.error("No matching token found for token: {}", token);
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Token không hợp lệ hoặc đã bị sử dụng"
            ));
        }

        VerificationToken vt = optionalToken.get();
        log.info("Token details - expiresAt: {}, confirmed: {}, user: {}", vt.getExpiresAt(), vt.isConfirmed(), vt.getUser().getEmail());

        if (vt.isConfirmed()) {
            log.warn("Token already confirmed for user: {}", vt.getUser().getEmail());
            return ResponseEntity.ok(Map.of(
                    "message", "Token đã được xác thực trước đó",
                    "redirectTo", "/auth/login"
            ));
        }


        if (vt.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.error("Token expired - user: {}, expiresAt: {}", vt.getUser().getEmail(), vt.getExpiresAt());
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Token xác thực đã hết hạn"
            ));
        }

        User user = vt.getUser();

        if (!user.isActive()) {
            user.setActive(true);
            try {
                User savedUser = userRepository.save(user);
                log.info("✅ Saved User: {}", savedUser);
                log.info("User activated successfully - email: {}, id: {}", savedUser.getEmail(), savedUser.getId());
            } catch (Exception e) {
                log.error("Failed to save user activation - email: {}, error: {}", user.getEmail(), e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "message", "Lỗi khi kích hoạt tài khoản: " + e.getMessage()
                ));
            }
        } else {
            log.info("User already active - email: {}", user.getEmail());
        }

        vt.setConfirmed(true);
        verificationTokenRepository.save(vt);
        log.info("Token marked as confirmed - user: {}", user.getEmail());

        return ResponseEntity.ok(Map.of(
                "message", "Xác nhận thành công",
                "redirectTo", "/auth/login"
        ));
    }

    @Transactional
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email không tồn tại"));
        }

        User user = optionalUser.get();
        Long userId = user.getId().longValue();

        // 🔹 Lấy OTP cũ (nếu có)
        VerificationToken existingToken = verificationTokenRepository
                .findByUserIdAndType(userId, "FORGOT_PASSWORD")
                .orElse(null);

        // 🔹 Nếu OTP cũ vẫn còn hạn → chặn spam
        if (existingToken != null && existingToken.getExpiresAt().isAfter(LocalDateTime.now())) {
            long secondsLeft = Duration.between(LocalDateTime.now(), existingToken.getExpiresAt()).getSeconds();
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("message", "Vui lòng đợi " + secondsLeft + " giây nữa để yêu cầu OTP mới"));
        }

        // 🔹 Nếu có OTP cũ nhưng đã hết hạn → xóa
        verificationTokenRepository.deleteByUserIdAndType(userId, "FORGOT_PASSWORD");

        // 🔹 Tạo OTP mới
        String otp = generateOtp();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = createdAt.plusMinutes(5);

        VerificationToken vt = VerificationToken.builder()
                .token(otp)
                .createdAt(createdAt)
                .expiresAt(expiresAt)
                .confirmed(false)
                .type("FORGOT_PASSWORD")
                .user(user)
                .build();
        verificationTokenRepository.save(vt);

        // 🔹 Gửi email OTP
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("huynhthibaotran276273@gmail.com");
        message.setTo(email);
        message.setSubject("Mã OTP khôi phục mật khẩu");
        message.setText(String.format(
                "Xin chào %s,\n\nBạn đang yêu cầu lấy lại mật khẩu cho tài khoản của mình.\n" +
                        "Mã OTP của bạn là: %s\n" +
                        "Mã này có hiệu lực trong 15 phút.\n\n" +
                        "Trân trọng.",
                user.getUsername(), otp
        ));

        mailSender.send(message);

        log.info("OTP sent to {}: {}", email, otp);
        return ResponseEntity.ok(Map.of(
                "message", "Mã OTP đã được gửi đến email của bạn",
                "success", true
        ));
    }


    @PostMapping("/resend-otp")
    @Transactional
    public ResponseEntity<Map<String, Object>> resendOtp(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email không tồn tại"));
        }

        User user = optionalUser.get();
        // Ép kiểu Integer sang Long
        Long userId = user.getId().longValue();
        verificationTokenRepository.deleteByUserIdAndType(userId, "FORGOT_PASSWORD");
        String otp = generateOtp();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = createdAt.plusMinutes(15);

        VerificationToken vt = VerificationToken.builder()
                .token(otp)
                .createdAt(createdAt)
                .expiresAt(expiresAt)
                .confirmed(false)
                .type("FORGOT_PASSWORD")
                .user(user)
                .build();
        verificationTokenRepository.save(vt);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP khôi phục mật khẩu (gửi lại)");
        message.setText("Mã OTP mới của bạn là: " + otp + ". Hiệu lực trong 15 phút.");
        mailSender.send(message);

        log.info("Resent OTP to {}: {}", email, otp);
        return ResponseEntity.ok(Map.of("message", "Mã OTP mới đã được gửi đến email của bạn"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody ForgotPasswordDTO dto) {
        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByTokenAndType(dto.getOtp(), "FORGOT_PASSWORD");

        if (optionalToken.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mã OTP không hợp lệ"));
        }

        VerificationToken vt = optionalToken.get();
        if (vt.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mã OTP đã hết hạn"));
        }

        if (vt.isConfirmed()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mã OTP đã được sử dụng"));
        }

        vt.setConfirmed(true);
        verificationTokenRepository.save(vt);
        return ResponseEntity.ok(Map.of("message", "Xác thực OTP thành công", "userId", vt.getUser().getId()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody ForgotPasswordDTO dto) {
        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByTokenAndType(dto.getOtp(), "FORGOT_PASSWORD");

        if (optionalToken.isEmpty() || !optionalToken.get().isConfirmed()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mã OTP không hợp lệ hoặc chưa xác thực"));
        }

        User user = optionalToken.get().getUser();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        verificationTokenRepository.delete(optionalToken.get());
        return ResponseEntity.ok(Map.of("message", "Đặt lại mật khẩu thành công. Vui lòng đăng nhập!"));
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsernameExists(@RequestParam("username") String username) {
        boolean exists = userRepository.findByUsername(username).isPresent();
        return ResponseEntity.ok(Map.of(
                "exists", exists,
                "message", exists ? "Username đã tồn tại" : "Username có thể sử dụng"
        ));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmailExists(@RequestParam("email") String email) {
        boolean exists = userRepository.findByEmail(email).isPresent();
        return ResponseEntity.ok(Map.of(
                "exists", exists,
                "message", exists ? "Email đã tồn tại" : "Email có thể sử dụng"
        ));
    }

    @GetMapping("/check-email-for-reset")
    public ResponseEntity<Map<String, Object>> checkEmailForPasswordReset(@RequestParam("email") String email) {
        boolean exists = userRepository.findByEmail(email).isPresent();
        return ResponseEntity.ok(Map.of(
                "exists", exists,
                "message", exists ? "Email tồn tại trong hệ thống" : "Email không tồn tại trong hệ thống"
        ));
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<Map<String, Object>> validateOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (email == null || otp == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "valid", false,
                    "message", "Email và OTP không được để trống"
            ));
        }

        // Kiểm tra email có tồn tại không
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "valid", false,
                    "message", "Email không tồn tại trong hệ thống"
            ));
        }

        User user = optionalUser.get();
        Long userId = user.getId().longValue();

        // Tìm OTP trong database
        Optional<VerificationToken> optionalToken = verificationTokenRepository
                .findByUserIdAndType(userId, "FORGOT_PASSWORD");

        if (optionalToken.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "valid", false,
                    "message", "Chưa có OTP nào được gửi cho email này"
            ));
        }

        VerificationToken vt = optionalToken.get();

        // Kiểm tra OTP có đúng không
        if (!vt.getToken().equals(otp)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "valid", false,
                    "message", "Mã OTP không đúng"
            ));
        }

        // Kiểm tra OTP có hết hạn không
        if (vt.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "valid", false,
                    "message", "Mã OTP đã hết hạn"
            ));
        }

        // Kiểm tra OTP đã được sử dụng chưa
        if (vt.isConfirmed()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "valid", false,
                    "message", "Mã OTP đã được sử dụng"
            ));
        }

        // OTP hợp lệ
        return ResponseEntity.ok(Map.of(
                "valid", true,
                "message", "Mã OTP hợp lệ",
                "userId", user.getId()
        ));
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // OTP 6 chữ số
        return String.valueOf(otp);
    }

}