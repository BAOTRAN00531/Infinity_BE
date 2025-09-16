package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.VerificationToken;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.dto.RegisterDTO;
import com.example.infinityweb_be.domain.dto.user.PasswordUpdate;
import com.example.infinityweb_be.domain.dto.user.UserProfileUpdate;
import com.example.infinityweb_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService tokenService; // ✅ Dùng service thay vì repo
    private final MailService mailService;
    @Value("${frontend.base-url}")
    private String frontendBaseUrl;


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

        // Thêm kiểm tra username
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username đã được sử dụng");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Logic kiểm tra và gán role vẫn giữ nguyên
        String requestedRole = dto.getRole() != null && !dto.getRole().isEmpty() ? dto.getRole().toLowerCase() : "student";
        if ("admin".equalsIgnoreCase(requestedRole)) {
            throw new RuntimeException("Không thể đăng ký với vai trò ADMIN. Vui lòng liên hệ quản trị viên.");
        }
        user.setRole(requestedRole);
        user.setActive(false);
        User savedUser = userRepository.save(user);

        sendVerificationEmail(savedUser);

        return savedUser;
    }

    public void sendVerificationEmail(User user) {
        // ✅ Tạo token và lưu vào DB qua VerificationTokenService
        VerificationToken token = tokenService.createVerificationToken(user);

        String link = frontendBaseUrl + "/verify-email?token=" + token.getToken();
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

    // GG FB
    public User findOrCreateOAuthUser(String email, String name, String avatarUrl) {
        return userRepository.findByEmail(email).map(user -> {
            // Nếu avatar cũ khác avatar mới → cập nhật
            if (!avatarUrl.equals(user.getAvatar())) {
                user.setAvatar(avatarUrl);
                userRepository.save(user);
            }

            // 🔒 CHỈ THÊM: nếu dữ liệu cũ isVip đang null → set false và lưu
            if (user.getIsVip() == null) {
                user.setIsVip(Boolean.FALSE);
                userRepository.save(user);
            }

            return user;
        }).orElseGet(() -> {
            User user = User.builder()
                    .email(email)
                    .username(email)
                    .fullName(name)
                    .avatar(avatarUrl)
                    .role("student")
                    .isActive(true)
                    .password("google_oauth")
                    .build();

            // 🔒 CHỈ THÊM: đảm bảo is_vip có giá trị khi INSERT
            user.setIsVip(Boolean.FALSE);

            return userRepository.save(user);
        });
    }

    // xac thuc nguoi dung
    public Integer getUserIdFromPrincipal(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();
    }

    // ✅ Thêm phương thức cập nhật hồ sơ
    public void updateUserProfile(String currentEmail, UserProfileUpdate profileUpdate) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        user.setFullName(profileUpdate.getFullName());
        user.setEmail(profileUpdate.getEmail());
        // Có thể thêm logic kiểm tra email đã tồn tại hay chưa
        userRepository.save(user);
    }

    // ✅ Thêm phương thức đổi mật khẩu
    public void updatePassword(String email, PasswordUpdate passwordUpdate) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(passwordUpdate.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu hiện tại không chính xác.");
        }

        // Mã hóa và cập nhật mật khẩu mới
        String encodedNewPassword = passwordEncoder.encode(passwordUpdate.getNewPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }
}
