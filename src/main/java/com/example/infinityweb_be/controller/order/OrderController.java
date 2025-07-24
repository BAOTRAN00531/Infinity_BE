package com.example.infinityweb_be.controller.order;

import com.example.infinityweb_be.domain.Order;
import com.example.infinityweb_be.domain.dto.order.CreateOrderRequest;
import com.example.infinityweb_be.domain.dto.order.OrderResponse;
import com.example.infinityweb_be.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }


    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestParam String orderCode,
                                          @RequestParam String status) {
        orderService.updateOrderStatus(orderCode, status);
        return ResponseEntity.ok("Order updated");
    }


    @GetMapping("/code/{orderCode}")
    public ResponseEntity<?> getOrderByCode(@PathVariable String orderCode) {
        OrderResponse response = orderService.getOrderByCode(orderCode);
        return ResponseEntity.ok(response);
    }


}
