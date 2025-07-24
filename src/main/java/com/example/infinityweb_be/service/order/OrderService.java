package com.example.infinityweb_be.service.order;

import com.example.infinityweb_be.domain.Order;
import com.example.infinityweb_be.domain.OrderDetail;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.course.Course;
import com.example.infinityweb_be.domain.dto.order.CreateOrderRequest;
import com.example.infinityweb_be.domain.dto.order.OrderDetailDTO;
import com.example.infinityweb_be.domain.dto.order.OrderResponse;
import com.example.infinityweb_be.domain.dto.order.OrderStatus;
import com.example.infinityweb_be.repository.CourseRepository;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.repository.order.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;


    public OrderResponse createOrder(CreateOrderRequest req) {

        Course course = courseRepository.findById(req.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Order order = Order.builder()
                .course(course)
                .totalAmount(course.getPrice()) // cần thêm field price trong Course
                .orderCode(generateOrderCode())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(1000000)) // ví dụ 1tr
                .build();

        OrderDetail detail = OrderDetail.builder()
                .order(order)
                .serviceName("Unlock khóa học 1 năm")
                .serviceDesc("Mở khóa tất cả nội dung trong 12 tháng")
                .price(BigDecimal.valueOf(1000000))
                .build();

        order.setOrderDetails(List.of(detail));
        Order saved = orderRepository.save(order);
        return new OrderResponse(
                saved.getOrderCode(),
                saved.getStatus().name(),
                saved.getTotalAmount(),
                saved.getPaymentMethod(),
                saved.getOrderDate(),
                List.of(new OrderDetailDTO("Unlock khóa học 1 năm", "Mở khóa tất cả nội dung trong 12 tháng", BigDecimal.valueOf(1000000)))
        );

    }

    public void updateOrderStatus(String orderCode, String status) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);
    }

    private String generateOrderCode() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public OrderResponse getOrderByCode(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderDetailDTO> details = order.getOrderDetails().stream()
                .map(d -> new OrderDetailDTO(d.getServiceName(), d.getServiceDesc(), d.getPrice()))
                .toList();

        return new OrderResponse(
                order.getOrderCode(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getOrderDate(),
                details
        );
    }

}
