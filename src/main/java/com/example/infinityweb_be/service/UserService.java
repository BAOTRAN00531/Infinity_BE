package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.EmailVerificationToken;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.dto.RegisterDTO;
import com.example.infinityweb_be.repository.EmailVerificationTokenRepository;
import com.example.infinityweb_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final MailService mailService;


    /**
     * Truy vấn người dùng theo email (dùng để login).
     */
    public User handleGetAccountByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.orElse(null);
    }

    /**
     * Lưu người dùng mới, tự động mã hóa mật khẩu trước khi lưu.
     */
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Kiểm tra password người dùng có khớp hay không (dùng cho các tình huống custom).
     */
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
        user.setRole(dto.getRole().toLowerCase());
        user.setActive(false);
        User savedUser = userRepository.save(user);

        // Gửi email xác thực
        sendVerificationEmail(savedUser);

        return savedUser;
    }

    public void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verifyToken = new EmailVerificationToken(
                null,
                token,
                user,
                LocalDateTime.now().plusMinutes(30)
        );
        emailVerificationTokenRepository.save(verifyToken);

        String link = "http://localhost:8080/auth/verify-email?token=" + token;
        String subject = "Xác thực tài khoản InfinityWeb";
        String body = "Xin chào " + user.getUsername() + ",\n\n" +
                "Vui lòng bấm vào đường link dưới đây để xác thực tài khoản:\n" +
                link + "\n\n" +
                "Link có hiệu lực trong 30 phút.";

        mailService.sendEmail(user.getEmail(), subject, body);
    }
    public EmailVerificationToken getEmailVerificationToken(String token) {
        return emailVerificationTokenRepository.findByToken(token).orElse(null);
    }

    public void enableUser(User user) {
        user.setActive(true);
        userRepository.save(user);
    }

    public void deleteEmailVerificationToken(EmailVerificationToken token) {
        emailVerificationTokenRepository.delete(token);
    }


}
