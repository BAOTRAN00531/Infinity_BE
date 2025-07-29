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
import java.util.Optional;
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

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        Order order = Order.builder()
                .course(course)
                .user(user)
                .orderCode(generateOrderCode())
                .orderDate(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusMonths(12))
                .status(OrderStatus.PENDING)
                .paymentMethod(req.getPaymentMethod()) // OK vì cùng kiểu enum
                .totalAmount(course.getPrice())
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
                saved.getPaymentMethod().name(), // ✅ sửa tại đây
                saved.getOrderDate(),
                List.of(new OrderDetailDTO("Unlock khóa học 1 năm", "Mở khóa tất cả nội dung trong 12 tháng", BigDecimal.valueOf(1000000)))
        );

    }

//    public void updateOrderStatus(String orderCode, String status) {
//        Order order = orderRepository.findByOrderCode(orderCode)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//        order.setStatus(OrderStatus.PENDING);
//        orderRepository.save(order);
//    }


    public void updateOrderStatus(String orderCode, String status) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.valueOf(status.toUpperCase())); // Chuyển từ chuỗi sang enum
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
                order.getPaymentMethod().name(), // ✅ sửa tại đây
                order.getOrderDate(),
                details
        );

    }

    public Optional<Order> findByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }





    // check hoc vien
    public boolean hasUserPurchasedCourse(Integer userId, Integer courseId) {
        return orderRepository.hasValidOrderByUserId(userId, courseId);
    }


    public List<OrderResponse> getOrdersByUserId(Integer userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream().map(this::mapToResponse).toList();
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll(); // hoặc phân trang
        return orders.stream().map(this::mapToResponse).toList();
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderDetailDTO> details = order.getOrderDetails().stream()
                .map(d -> new OrderDetailDTO(d.getServiceName(), d.getServiceDesc(), d.getPrice()))
                .toList();

        return new OrderResponse(
                order.getOrderCode(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getPaymentMethod().name(),
                order.getOrderDate(),
                details
        );
    }





}
