package com.example.infinityweb_be.controller.client.order;

import com.example.infinityweb_be.domain.dto.order.OrderStatus;
import com.example.infinityweb_be.service.order.OrderService;
import com.example.infinityweb_be.service.order.VNPAY.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URI;

@RestController
@RequestMapping("/api/vnpay")
@RequiredArgsConstructor
public class VNPayController {

    private final OrderService orderService;
    private final VNPayService vnpayService;

    @GetMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam String orderCode, HttpServletRequest request) throws UnsupportedEncodingException {
        String paymentUrl = vnpayService.createPaymentUrl(orderCode, 1000000L, request);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(paymentUrl))
                .build();
    }


    @GetMapping("/return")
    public RedirectView vnpayReturn(@RequestParam String orderId, @RequestParam String result) {
        orderService.updateOrderStatus(orderId, "success".equals(result) ? OrderStatus.PAID.name() : OrderStatus.FAILED.name());
        return new RedirectView("http://localhost:3000/payment-success?orderId=" + orderId + "&result=" + result);
    }

}
