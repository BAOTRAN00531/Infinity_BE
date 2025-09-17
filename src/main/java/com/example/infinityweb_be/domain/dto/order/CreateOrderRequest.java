package com.example.infinityweb_be.domain.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
//    @NotNull
//    private Integer userId;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private Integer courseId;
}

