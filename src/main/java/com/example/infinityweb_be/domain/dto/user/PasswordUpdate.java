package com.example.infinityweb_be.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdate {
    private String currentPassword;
    private String newPassword;
}