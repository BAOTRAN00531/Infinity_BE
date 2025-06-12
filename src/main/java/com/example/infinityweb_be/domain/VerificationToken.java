package com.example.infinityweb_be.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_token")                // ← rõ ràng tên bảng
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024, nullable = false)       // ← 1024 ký tự để “dư dả”
    private String token;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean confirmed = false;

    /** EMAIL_CONFIRMATION, PASSWORD_RESET, ACCESS, REFRESH… */
    @Column(length = 50, nullable = false)
    private String type;

    /** Mỗi user có thể có nhiều token → ManyToOne */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}

