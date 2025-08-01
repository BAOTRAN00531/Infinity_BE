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
    private String courseName;
    private List<OrderDetailDTO> details;
    private String userName;



}
