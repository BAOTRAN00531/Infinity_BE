package com.example.infinityweb_be.controller.client.order;

import com.example.infinityweb_be.domain.dto.order.OrderStatus;
import com.example.infinityweb_be.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.view.RedirectView;

import java.net.URI;

@RestController
@RequestMapping("/api/vnpay")
@RequiredArgsConstructor
public class VNPayController {

    private final OrderService orderService;

    @GetMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam String orderCode) {
        // Giả sử em tự build URL (thực tế dùng SDK của VNPAY hoặc tự ký)
        String redirectUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?orderId="
                + orderCode + "&amount=1000000&returnUrl=http://localhost:8080/api/vnpay/return";
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    @GetMapping("/return")
    public RedirectView vnpayReturn(@RequestParam String orderId, @RequestParam String result) {
        orderService.updateOrderStatus(orderId, "success".equals(result) ? OrderStatus.PAID.name() : OrderStatus.FAILED.name());
        return new RedirectView("http://localhost:3000/payment-success?orderId=" + orderId + "&result=" + result);
    }

}
