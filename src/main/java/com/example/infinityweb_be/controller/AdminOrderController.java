package com.example.infinityweb_be.controller;


import com.example.infinityweb_be.domain.dto.order.OrderDetailDTO;
import com.example.infinityweb_be.domain.dto.order.OrderResponse;
import com.example.infinityweb_be.service.order.OrderService;
import com.example.infinityweb_be.service.orderdetail.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class AdminOrderController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/history")
    public ResponseEntity<?> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve")
    public ResponseEntity<?> approveOrder(@RequestParam String orderCode) {
        orderService.approveOrder(orderCode);
        return ResponseEntity.ok("Đơn đã được duyệt");
    }

    @GetMapping("/detail/{orderCode}")
    public ResponseEntity<?> getOrderDetailsForAdmin(@PathVariable String orderCode) {
        List<OrderDetailDTO> details = orderDetailService.getOrderDetailsByOrderCode(orderCode);
        return ResponseEntity.ok(details);
    }

    @DeleteMapping("/delete/{orderCode}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderCode) {
        orderService.deleteOrderByCode(orderCode);
        return ResponseEntity.noContent().build();
    }

}
