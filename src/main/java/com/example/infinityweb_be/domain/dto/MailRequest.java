package com.example.infinityweb_be.domain.dto;

import lombok.Data;

@Data

public class MailRequest {
    private String to;
    private String subject;
    private String body;
}
