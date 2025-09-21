package com.example.infinityweb_be.domain.dto.user;

// src/main/java/com/example/infinityweb_be/domain/dto/user/UserProfileUpdate.java
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdate {
    private String fullName;
    private String email;
    private String avatar;
}
