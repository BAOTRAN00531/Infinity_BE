package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.RefreshToken;
import com.example.infinityweb_be.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Modifying
    @Transactional
    void deleteByUser(User user);

    Optional<RefreshToken> findByToken(String token);
}
//Quên mật khẩu+đặt lại mật khẩu
//Tự động làm mới token (refresh) bằng interceptor phía client (nếu dùng SPA).
//Lịch sử đăng nhập (Optional)
//xác thực 2 lớp
//Xác thực OAuth2 / Đăng nhập với Google, Facebook
// Giới hạn đăng nhập sai