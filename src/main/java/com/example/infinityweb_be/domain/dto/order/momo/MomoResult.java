package com.example.infinityweb_be.domain.dto.order.momo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MomoResult {
    private String partnerCode;
    private String orderId;
    private String requestId;
    private String amount;
    private String orderInfo;
    private String orderType;
    private String transId;
    private int resultCode;
    private String message;
    private String payType;
    private long responseTime;
    private String extraData;
    private String signature;
}