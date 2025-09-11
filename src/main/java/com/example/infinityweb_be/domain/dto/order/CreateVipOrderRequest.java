package com.example.infinityweb_be.domain.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVipOrderRequest {
    @NotNull
    private Integer userId;

    @NotBlank
    private String paymentMethod;

    @NotNull
    private Integer durationInMonths; // 1, 3, 6, 12
}