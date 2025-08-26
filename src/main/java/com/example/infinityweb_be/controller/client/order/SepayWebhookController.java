package com.example.infinityweb_be.controller.client.order;

import com.example.infinityweb_be.domain.Order;
import com.example.infinityweb_be.domain.dto.order.OrderStatus;
import com.example.infinityweb_be.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/sepay")
@RequiredArgsConstructor
@Slf4j
public class SepayWebhookController {

    private final OrderService orderService;

    @Value("${sepay.api-key}")
    private String sepayApiKey;

    // ⚡ Cấu hình cố định của tài khoản SePay bạn
    private static final String BANK_NAME = "MBBank";
    private static final String ACCOUNT_NUMBER = "0964618706";
    private static final String ACCOUNT_NAME = "InfinityCat";
    private static final String VIRTUAL_ACCOUNT = "VQRQADPFL6166";

    // ✅ Gửi thông tin thanh toán để FE render giao diện + QR Code
    @GetMapping("/pay")
    public ResponseEntity<Map<String, Object>> getSepayInfo(@RequestParam String orderCode) {
        Order order = orderService.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        BigDecimal amount = order.getTotalAmount();

        String qrUrl = String.format(
                "https://qr.sepay.vn/img?acc=%s&bank=%s&amount=%s&des=%s",
                VIRTUAL_ACCOUNT, // ✅ Dùng VA thay vì tài khoản chính
                BANK_NAME,
                amount.stripTrailingZeros().toPlainString(),
                order.getOrderCode()
        );

        Map<String, Object> response = Map.of(
                "bankName", BANK_NAME,
                "accountNumber", VIRTUAL_ACCOUNT, // ✅ Cập nhật đúng VA
                "accountName", ACCOUNT_NAME,
                "virtualAccount", VIRTUAL_ACCOUNT,
                "amount", amount,
                "transferContent", order.getOrderCode(),
                "qrCodeUrl", qrUrl
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getOrderStatus(@RequestParam String orderCode) {
        Order order = orderService.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return ResponseEntity.ok(Map.of(
                "status", order.getStatus().name(),
                "orderDate", order.getOrderDate(),
                "paymentMethod", order.getPaymentMethod().name(),
                "totalAmount", order.getTotalAmount()
        ));
    }

    // Helper method để extract mã đơn hàng từ content
    private String extractOrderCode(String content) {
        if (content == null) return null;

        // Ví dụ mã đơn hàng là DH + 8 chữ số
        Pattern pattern = Pattern.compile("\\bDH\\d{8}\\b");
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group() : null;
    }



    // ✅ Xử lý webhook từ SePay gọi về (IPN)
    @PostMapping
    public ResponseEntity<String> handleSepayIpn(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody SepayWebhookRequest request
    ) {
        log.info("📥 [SePay Webhook] Payload: {}", request);
        log.info("🔑 Authorization header: {}", authorizationHeader);

        log.info("💬 Nội dung chuyển khoản: {}", request.getContent());
        log.info("💬 Mô tả giao dịch: {}", request.getDescription());


        // 1. Xác thực API Key
        if (authorizationHeader == null || !authorizationHeader.equals("Apikey " + sepayApiKey)) {
            log.warn("[SePay] Unauthorized webhook - invalid API key.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        // 2. Trích xuất mã đơn hàng
        String orderCode = request.getCode();
        if (orderCode == null || orderCode.isBlank()) {
            orderCode = extractOrderCode(request.getContent());
            if (orderCode == null) {
                return ResponseEntity.badRequest().body("Thiếu mã đơn hàng.");
            }
        }

        // 3. Tìm đơn hàng
        Order order = orderService.findByOrderCode(orderCode).orElse(null);
        if (order == null) {
            log.warn("[SePay] Webhook: Order not found - {}", orderCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        // 4. Tránh xử lý lại đơn đã thanh toán
        if (order.getStatus() == OrderStatus.PAID) {
            return ResponseEntity.ok("Order already paid.");
        }

        // ✅ 5. Mặc định: bất kỳ webhook đến đều là PAID
        orderService.updateOrderStatus(order.getOrderCode(), OrderStatus.PAID.name());
        log.info("[SePay] Order {} marked as PAID via webhook", order.getOrderCode());

        return ResponseEntity.ok("OK");
    }


    @PostMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancelPayment(@RequestParam String orderCode) {
        Order order = orderService.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.PAID) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Đơn hàng đã thanh toán, không thể hủy"
            ));
        }

        orderService.updateOrderStatus(orderCode, OrderStatus.CANCELLED.name());

        return ResponseEntity.ok(Map.of(
                "message", "Hủy thanh toán thành công",
                "status", OrderStatus.CANCELLED.name(),
                "orderCode", orderCode
        ));
    }


}
