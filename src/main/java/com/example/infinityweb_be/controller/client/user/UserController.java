package com.example.infinityweb_be.controller.client.user;

// src/main/java/com/example/infinityweb_be.controller.client.user/UserController.java

import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.dto.user.UserDto;
import com.example.infinityweb_be.domain.dto.user.UserProfileUpdate;
import com.example.infinityweb_be.domain.dto.user.PasswordUpdate;
import com.example.infinityweb_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getLoggedInUser(Principal principal) {
        String userEmail = principal.getName();
        User user = userService.handleGetAccountByEmail(userEmail);
        if (user != null) {
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .avatar(user.getAvatar())
                    .role(user.getRole())
                    .isVip(user.getIsVip())
                    .vipExpiryDate(user.getVipExpiryDate())
                    .build();
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Endpoint POST để cập nhật thông tin chung
    @PostMapping("/me/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileUpdate profileUpdate, Principal principal) {
        try {
            userService.updateUserProfile(principal.getName(), profileUpdate);
            return ResponseEntity.ok("Cập nhật hồ sơ thành công!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Endpoint POST để đổi mật khẩu
    @PostMapping("/me/password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdate passwordUpdate, Principal principal) {
        try {
            userService.updatePassword(principal.getName(), passwordUpdate);
            return ResponseEntity.ok("Đổi mật khẩu thành công!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}