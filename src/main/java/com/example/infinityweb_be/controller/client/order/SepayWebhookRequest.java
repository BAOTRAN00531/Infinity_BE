package com.example.infinityweb_be.controller.client.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SepayWebhookRequest {
    private Long id;
    private String gateway;
    private String transactionDate;
    private String accountNumber;
    private String code;
    private String content;
    private String transferType;
    private Long transferAmount;
    private Long accumulated;
    private String subAccount;
    private String referenceCode;
    private String description;
    private String status; // PAID / FAILED / EXPIRED
}

