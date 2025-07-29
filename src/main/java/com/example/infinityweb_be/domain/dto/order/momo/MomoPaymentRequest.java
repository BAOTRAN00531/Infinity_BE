package com.example.infinityweb_be.domain.dto.order.momo;

import com.example.infinityweb_be.domain.Order;
import lombok.Data;

@Data
public class MomoPaymentRequest {
    private int userId;
    private int courseId;


}
