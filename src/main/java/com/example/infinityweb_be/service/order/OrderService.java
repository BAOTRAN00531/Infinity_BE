package com.example.infinityweb_be.service.order;

import com.example.infinityweb_be.common.NotFoundException;
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
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
                .serviceName("Mua khóa học: " + course.getName())
                .serviceDesc("Truy cập toàn bộ nội dung khóa học \"" + course.getName() + "\" trong 12 tháng.")
                .price(course.getPrice())
                .build();

        order.setOrderDetails(List.of(detail));
        Order saved = orderRepository.save(order);
        return mapToResponse(saved);

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

        return mapToResponse(order);
    }


    private OrderResponse mapToResponse(Order order) {
        List<OrderDetailDTO> details = order.getOrderDetails().stream()
                .map(d -> new OrderDetailDTO(d.getServiceName(), d.getServiceDesc(), d.getPrice()))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getOrderCode(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getPaymentMethod().name(),
                order.getOrderDate(),
                order.getCourse().getName(),
                details,
                order.getUser().getUsername() // ← thêm
        );
    }


    public Optional<Order> findByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }





    // dùng trong backend khi phân quyền xem nội dung
    public boolean hasUserPurchasedCourse(Integer userId, Integer courseId) {
        return orderRepository.hasValidOrderByUserId(userId, courseId);
    }



    public List<OrderResponse> getOrdersByUserId(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


//dùng để truy xuất các khóa học học viên đã mua
    public List<OrderResponse> getOrdersByUserIdAndStatus(Integer userId, String status) {
        List<Order> orders;
        if (status != null) {
            orders = orderRepository.findByUserIdAndStatus(userId, OrderStatus.valueOf(status.toUpperCase()));
        } else {
            orders = orderRepository.findByUserId(userId);
        }

        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
    }



    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll(); // hoặc phân trang
        return orders.stream().map(this::mapToResponse).toList();
    }




    public void cancelOrder(String orderCode, Integer userId) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn"));

        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Bạn không có quyền hủy đơn này");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Chỉ có thể hủy đơn ở trạng thái PENDING");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }


    public void approveOrder(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Chỉ duyệt đơn ở trạng thái PENDING");
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }


    public void deleteOrderByCode(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }

}
