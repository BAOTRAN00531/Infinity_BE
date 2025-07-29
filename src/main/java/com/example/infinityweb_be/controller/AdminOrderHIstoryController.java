package com.example.infinityweb_be.controller;


import com.example.infinityweb_be.domain.dto.order.OrderResponse;
import com.example.infinityweb_be.service.order.OrderService;
import com.example.infinityweb_be.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class AdminOrderHIstoryController {

    private final OrderService orderService;

    @GetMapping("/admin/history")
    public ResponseEntity<?> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

}
