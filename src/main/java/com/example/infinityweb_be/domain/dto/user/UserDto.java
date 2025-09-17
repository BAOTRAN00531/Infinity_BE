package com.example.infinityweb_be.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private String avatar;
    private String role;
    private Boolean isVip; // ✅ Thêm trường này
    private LocalDateTime vipExpiryDate; // ✅ Thêm trường này
}