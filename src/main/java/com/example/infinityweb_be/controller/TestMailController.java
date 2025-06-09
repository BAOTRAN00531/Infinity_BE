package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.dto.MailRequest;
import com.example.infinityweb_be.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

public class TestMailController {
    private final MailService mailService;

    @PostMapping("/send-mail")
    public ResponseEntity<String> sendMail(@RequestBody MailRequest request) {
        mailService.sendEmail(request.getTo(), request.getSubject(), request.getBody());
        return ResponseEntity.ok("Mail đã được gửi đến: " + request.getTo());
    }
}
