package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.EmailVerificationToken;
import com.example.infinityweb_be.domain.RefreshToken;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.dto.LoginDTO;
import com.example.infinityweb_be.domain.dto.RegisterDTO;
import com.example.infinityweb_be.domain.dto.ResLoginDTO;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.security.JwtService;
import com.example.infinityweb_be.security.RefreshTokenService;
import com.example.infinityweb_be.service.UserDetailCustom;
import com.example.infinityweb_be.service.UserService;
import com.example.infinityweb_be.util.SecurityUtil;
import com.example.infinityweb_be.util.anotation.ApiMessage;
import com.example.infinityweb_be.util.error.IdInvalidException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @Value("${assigment_java6.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpirySeconds;

    /**
     * Đăng nhập: trả về access token và set cookie refresh token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = ((UserDetailCustom) userDetails).getUser();

        // Check nếu chưa xác thực email
        if (!user.isActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Tài khoản chưa được xác thực email. Vui lòng kiểm tra email để xác thực.");
        }

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        ResLoginDTO responseDTO = new ResLoginDTO();
        responseDTO.setUserp(new ResLoginDTO.UserLogin(user.getId(), user.getEmail(), user.getUsername()));
        responseDTO.setAccess_token(accessToken);

        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * Lấy access token mới từ refresh token (qua cookie)
     */
    @GetMapping("/refresh-token")
    public ResponseEntity<ResLoginDTO> refresh(@CookieValue("refresh_token") String refreshToken) {
        var optional = refreshTokenService.validateRefreshToken(refreshToken);
        if (optional.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        User user = optional.get().getUser();

        // ✅ Sửa lỗi: truyền đúng kiểu UserDetails
        String accessToken = jwtService.generateAccessToken(new UserDetailCustom(user));

        ResLoginDTO response = new ResLoginDTO();
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                user.getId(), user.getEmail(), user.getUsername()
        );
        response.setUserp(userLogin);
        response.setAccess_token(accessToken);

        return ResponseEntity.ok(response);
    }


    /**
     * Đăng xuất: xoá refresh token trong DB và cookie
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        String email = SecurityUtil.getCurrentUserLogin().orElse(null);
        if (email != null) {
            User user = userService.handleGetAccountByEmail(email);
            if (user != null) {
                refreshTokenService.deleteTokenByUser(user);  // <- cần transactional
            }
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

    /**
     * Lấy thông tin tài khoản từ access token
     */
    @GetMapping("/account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().orElse(null);
        if (email == null) {
            return ResponseEntity.status(401).build();
        }

        User currentUser = userService.handleGetAccountByEmail(email);
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                currentUser.getId(), currentUser.getEmail(), currentUser.getUsername());

        return ResponseEntity.ok(userLogin);
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is public endpoint";
    }

    @GetMapping("/secure")
    public String secureEndpoint() {
        return "This is secure endpoint";
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
        try {
            userService.registerNewUser(registerDTO);
            return ResponseEntity.ok("Đăng ký thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        // Tìm token trong DB
        EmailVerificationToken verificationToken = userService.getEmailVerificationToken(token);
        if (verificationToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token xác thực không hợp lệ hoặc đã bị sử dụng");
        }

        // Kiểm tra thời gian hết hạn token
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token xác thực đã hết hạn");
        }

        // Lấy user
        User user = verificationToken.getUser();

        if (user.isActive()) {
            return ResponseEntity.badRequest().body("Tài khoản đã được xác thực trước đó");
        }

        // Kích hoạt tài khoản user
        userService.enableUser(user);

        // Xoá token sau khi xác thực thành công
        userService.deleteEmailVerificationToken(verificationToken);

        return ResponseEntity.ok("Xác thực email thành công. Bạn có thể đăng nhập ngay bây giờ.");
    }


}
