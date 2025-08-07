package com.example.infinityweb_be.controller.client.order;

import com.example.infinityweb_be.common.JwtUtil;
import com.example.infinityweb_be.domain.Order;
import com.example.infinityweb_be.domain.dto.order.CreateOrderRequest;
import com.example.infinityweb_be.domain.dto.order.OrderDetailDTO;
import com.example.infinityweb_be.domain.dto.order.OrderResponse;
import com.example.infinityweb_be.service.order.OrderService;
import com.example.infinityweb_be.service.orderdetail.OrderDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestParam String orderCode,
                                          @RequestParam String status) {
        orderService.updateOrderStatus(orderCode, status);
        return ResponseEntity.ok("Order updated");
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/code/{orderCode}")
    public ResponseEntity<?> getOrderByCode(@PathVariable String orderCode) {
        OrderResponse response = orderService.getOrderByCode(orderCode);
        return ResponseEntity.ok(response);
    }


//    ==== Xem lịch sử đơn hàng
@PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/history")
public ResponseEntity<?> getUserOrderHistory(@RequestParam(required = false) String status,
                                             HttpServletRequest request) {
    String userId = extractUserIdFromToken(request);
    List<OrderResponse> orders = orderService.getOrdersByUserIdAndStatus(Integer.parseInt(userId), status);
    return ResponseEntity.ok(orders);
}

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/detail/{orderCode}")
    public ResponseEntity<?> getOrderDetails(@PathVariable String orderCode) {
        List<OrderDetailDTO> details = orderDetailService.getOrderDetailsByOrderCode(orderCode);
        return ResponseEntity.ok(details);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestParam String orderCode, HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        orderService.cancelOrder(orderCode, Integer.parseInt(userId));
        return ResponseEntity.ok("Đã hủy đơn hàng");
    }



    private String extractUserIdFromToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Token không hợp lệ hoặc thiếu");
        }

        String token = header.substring(7);
        // Ví dụ giải mã bằng JWT utils bạn đã có
        return JwtUtil.extractUserId(token); // ⚠️ bạn cần có class JwtUtil
    }
}
