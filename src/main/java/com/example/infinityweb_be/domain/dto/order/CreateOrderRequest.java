package com.example.infinityweb_be.domain.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {
    @NotNull
    private Integer userId;

    @NotBlank
    private String paymentMethod;

    @NotNull
    private Integer courseId; // Bổ sung cho bước 4
}
