package com.example.infinityweb_be.domain.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponse {
    private String orderCode;
    private String status;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private LocalDateTime orderDate;
    private List<OrderDetailDTO> details;

    public OrderResponse(String orderCode, String status, BigDecimal totalAmount, String paymentMethod) {
        this.orderCode = orderCode;
        this.status = status;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
    }
}
