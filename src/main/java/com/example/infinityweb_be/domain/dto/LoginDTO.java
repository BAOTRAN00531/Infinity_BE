package com.example.infinityweb_be.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {
    @NotBlank(message = "Không được để trống username")
  private String username;
    @NotBlank(message = "Không được để trống password")
    private  String password;

}
