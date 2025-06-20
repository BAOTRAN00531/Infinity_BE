package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.VerificationToken;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.dto.RegisterDTO;
import com.example.infinityweb_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService tokenService; // ✅ Dùng service thay vì repo
    private final MailService mailService;

    public User handleGetAccountByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User registerNewUser(RegisterDTO dto) {
        if (existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Chỉ cho phép role "student" hoặc các role khác không phải "ADMIN" khi đăng ký
        String requestedRole = dto.getRole() != null && !dto.getRole().isEmpty() ? dto.getRole().toLowerCase() : "student";
        if ("admin".equalsIgnoreCase(requestedRole)) {
            throw new RuntimeException("Không thể đăng ký với vai trò ADMIN. Vui lòng liên hệ quản trị viên.");
        }
        user.setRole(requestedRole);
        user.setActive(false);
        User savedUser = userRepository.save(user);

        // ✅ Gọi tokenService để tạo token và gửi mail
        sendVerificationEmail(savedUser);

        return savedUser;
    }

    public void sendVerificationEmail(User user) {
        // ✅ Tạo token và lưu vào DB qua VerificationTokenService
        VerificationToken token = tokenService.createVerificationToken(user);

        String link = "http://localhost:3000/verify-email?token=" + token.getToken();
        String subject = "Xác thực tài khoản InfinityWeb";
        String body = "Xin chào " + user.getUsername() + ",\n\n" +
                "Vui lòng bấm vào đường link dưới đây để xác thực tài khoản:\n" +
                link + "\n\n" +
                "Link có hiệu lực trong 10 phút.";

        mailService.sendEmail(user.getEmail(), subject, body);
    }

    public VerificationToken getVerificationToken(String token) {
        return tokenService.validateRefreshToken(token).orElse(null);
    }

    public void enableUser(User user) {
        user.setActive(true);
        userRepository.save(user);
    }

    public void deleteVerificationToken(VerificationToken token) {
        tokenService.deleteRefreshToken(token.getUser()); // ✅ hoặc bạn dùng `tokenService.delete(token)` nếu muốn xóa
    }

    public Optional<User> findByEmailOrUsername(String input) {
        return input.contains("@")
                ? userRepository.findByEmail(input)
                : userRepository.findByUsername(input);
    }

    // Phương thức phụ (tùy chọn) để admin tạo tài khoản khác
    public User createUserByAdmin(User user, String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new RuntimeException("Chỉ admin mới có quyền tạo tài khoản.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role.toLowerCase()); // Cho phép set "ADMIN" nếu cần
        user.setActive(true); // Admin có thể kích hoạt ngay
        return userRepository.save(user);
    }
}