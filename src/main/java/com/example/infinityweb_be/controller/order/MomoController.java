package com.example.infinityweb_be.controller.order;

import com.example.infinityweb_be.domain.Order;
import com.example.infinityweb_be.domain.dto.order.OrderStatus;
import com.example.infinityweb_be.domain.dto.order.momo.MomoPaymentRequest;
import com.example.infinityweb_be.domain.dto.order.momo.MomoResult;
import com.example.infinityweb_be.service.order.OrderService;
import com.example.infinityweb_be.service.order.momo.MomoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/momo")
public class MomoController {

    private final MomoService momoService;
    private final OrderService orderService;



    @GetMapping("/pay")
    public ResponseEntity<?> payWithMomo(@RequestParam String orderCode) {
        if (orderCode == null || orderCode.isBlank()) {
            return ResponseEntity.badRequest().body("Thiếu mã đơn hàng.");
        }
        Order order = orderService.findByOrderCode(orderCode) // ✅ gọi đúng instance
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String redirectUrl = momoService.buildRedirectUrl(order);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();
    }



    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody MomoPaymentRequest request) {
        String paymentUrl = momoService.createMomoPayment(request);
        return ResponseEntity.ok(Collections.singletonMap("paymentUrl", paymentUrl));
    }

    @PostMapping("/notify")
    public ResponseEntity<String> handleMomoNotify(@RequestBody MomoResult result) {
        boolean isValid = momoService.verifySignature(result);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        orderService.updateOrderStatus(result.getOrderId(), OrderStatus.PAID.name());


        return ResponseEntity.ok("{\"message\":\"Received\"}");
    }


}
