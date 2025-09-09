package com.example.infinityweb_be.service.order;

import com.example.infinityweb_be.common.NotFoundException;
import com.example.infinityweb_be.domain.Order;
import com.example.infinityweb_be.domain.OrderDetail;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.course.Course;
import com.example.infinityweb_be.domain.dto.order.*;
import com.example.infinityweb_be.repository.CourseRepository;
import com.example.infinityweb_be.repository.UserRepository;

import com.example.infinityweb_be.repository.enrollment.EnrollmentRepository;
import com.example.infinityweb_be.repository.order.OrderDetailRepository;
import com.example.infinityweb_be.repository.order.OrderRepository;

import com.example.infinityweb_be.service.Enrollment.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentService enrollmentService;
    private final OrderDetailRepository orderDetailRepository; // ✅ Cần thêm dòng này

//Tạo đơn hàng mới
@Transactional // ✅ Thêm @Transactional để đảm bảo tất cả hoạt động được thực hiện trong một giao dịch
public OrderResponse createOrder(CreateOrderRequest req) {
    Course course = courseRepository.findById(req.getCourseId())
            .orElseThrow(() -> new RuntimeException("Course not found"));

    User user = userRepository.findById(req.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Order order = Order.builder()
            .user(user)
            .orderCode(generateOrderCode())
            .orderDate(LocalDateTime.now())
            .expiryDate(LocalDateTime.now().plusMonths(12))
            .status(OrderStatus.PENDING)
            .paymentMethod(req.getPaymentMethod())
            .totalAmount(course.getPrice())
            .build();

    // Lưu Order trước để có ID
    orderRepository.save(order);

    // Khởi tạo một đối tượng OrderDetail và liên kết với Order đã lưu
    OrderDetail detail = new OrderDetail();
    detail.setOrder(order);
    detail.setCourse(course);
    detail.setServiceName("Mua khóa học: " + course.getName());
    detail.setServiceDesc("Truy cập toàn bộ nội dung khóa học \"" + course.getName() + "\" trong 12 tháng.");
    detail.setPrice(course.getPrice());

    // Lưu OrderDetail
    orderDetailRepository.save(detail); // ✅ Cần inject và sử dụng OrderDetailRepository

    // Cập nhật lại danh sách OrderDetails trong đối tượng Order
    order.getOrderDetails().add(detail);

    // Trả về kết quả
    return mapToResponse(order);
}

//    public void updateOrderStatus(String orderCode, String status) {
//        Order order = orderRepository.findByOrderCode(orderCode)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//        order.setStatus(OrderStatus.PENDING);
//        orderRepository.save(order);
//    }

//Cập nhật trạng thái đơn hàng
    public void updateOrderStatus(String orderCode, String status) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.valueOf(status.toUpperCase())); // Chuyển từ chuỗi sang enum
        orderRepository.save(order);
    }

  //  Tạo mã đơn hàng
    private String generateOrderCode() {
        return "DH" + String.valueOf(System.currentTimeMillis()).substring(5);
    }



   // Lấy thông tin đơn hàng theo mã
    public OrderResponse getOrderByCode(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapToResponse(order);
    }

    //Mapping từ Entity → DTO
// Mapping từ Entity → DTO
    private OrderResponse mapToResponse(Order order) {
        List<OrderDetailDTO> details = order.getOrderDetails().stream()
                .map(d -> new OrderDetailDTO(d.getServiceName(), d.getServiceDesc(), d.getPrice()))
                .collect(Collectors.toList());

        // ✅ Sửa lỗi ở đây
        String courseName = null; // Khởi tạo với giá trị null

        // Lấy OrderDetail đầu tiên nếu có
        if (!order.getOrderDetails().isEmpty()) {
            OrderDetail firstDetail = order.getOrderDetails().get(0);
            // Kiểm tra nếu OrderDetail có liên kết với một khóa học (không phải null)
            if (firstDetail.getCourse() != null) {
                courseName = firstDetail.getCourse().getName();
            }
        }

        return new OrderResponse(
                order.getOrderCode(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getPaymentMethod().name(),
                order.getOrderDate(),
                courseName, // ✅ Sử dụng biến courseName đã xử lý
                details,
                order.getUser().getUsername()
        );
    }

//Tìm đơn hàng theo mã (trả Optional)
    public Optional<Order> findByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }





//Kiểm tra người dùng đã mua khóa học chưa
    public boolean hasUserPurchasedCourse(Integer userId, Integer courseId) {
        return enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
    }


// Lấy tất cả đơn hàng theo người dùng
    public List<OrderResponse> getOrdersByUserId(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


//Lọc đơn hàng theo người dùng và trạng thái
    public List<OrderResponse> getOrdersByUserIdAndStatus(Integer userId, String status) {
        List<Order> orders;
        if (status != null) {
            orders = orderRepository.findByUserIdAndStatus(userId, OrderStatus.valueOf(status.toUpperCase()));
        } else {
            orders = orderRepository.findByUserId(userId);
        }

        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


//Lấy tất cả đơn hàng
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll(); // hoặc phân trang
        return orders.stream().map(this::mapToResponse).toList();
    }



// Hủy đơn hàng
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


    /**
     * Tạo đơn hàng kích hoạt tài khoản VIP
     * @param req Thông tin yêu cầu từ client (userId, paymentMethod, durationInMonths)
     * @return OrderResponse thông tin đơn hàng đã tạo
     */
    @Transactional
    public OrderResponse createVipOrder(CreateVipOrderRequest req) {
        // 1. Tìm thông tin User
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        // 2. Xác định giá dịch vụ VIP dựa trên thời gian
        BigDecimal price = getVipPrice(req.getDurationInMonths());

        // 3. Xây dựng đối tượng Order
        Order order = Order.builder()
                .user(user)
                .orderCode(generateOrderCode())
                .orderDate(LocalDateTime.now())
                // Thời gian hết hạn của đơn hàng VIP sẽ khác với khóa học.
                // Có thể đặt là 1 tháng để chờ thanh toán
                .expiryDate(LocalDateTime.now().plusMonths(1))
                .status(OrderStatus.PENDING)
                .paymentMethod(PaymentMethod.valueOf(req.getPaymentMethod().toUpperCase())) // FIX: Chuyển đổi String sang Enum
                .totalAmount(price)
                .build();

        // 4. Lưu Order vào cơ sở dữ liệu
        orderRepository.save(order);

        // 5. Xây dựng và lưu OrderDetail cho dịch vụ VIP
        OrderDetail detail = new OrderDetail();
        detail.setOrder(order);
        detail.setServiceName("Kích hoạt tài khoản VIP");
        detail.setServiceDesc("Truy cập toàn bộ khóa học trong " + req.getDurationInMonths() + " tháng.");
        detail.setPrice(price);
        // VÌ ĐÂY LÀ ĐƠN HÀNG VIP, KHÔNG LIÊN QUAN ĐẾN KHÓA HỌC CỤ THỂ,
        // NÊN KHÔNG CẦN SET COURSE VÀO ORDERDETAIL
        // detail.setCourse(course); // BỎ DÒNG NÀY
        orderDetailRepository.save(detail);

        // 6. Cập nhật danh sách chi tiết đơn hàng (nếu cần)
        order.getOrderDetails().add(detail);

        // 7. Trả về đối tượng Response
        return mapToResponse(order);
    }

    /**
     * Lấy giá tiền cho gói VIP
     * @param months Số tháng đăng ký VIP
     * @return BigDecimal giá tiền tương ứng
     */
    private BigDecimal getVipPrice(int months) {
        // Đây là logic giá tiền giả định. Bạn cần thay thế bằng giá thực tế
        switch (months) {
            case 1:
                return new BigDecimal("500000"); // 500k
            case 3:
                return new BigDecimal("1200000"); // 1tr2
            case 6:
                return new BigDecimal("2000000"); // 2tr
            case 12:
                return new BigDecimal("3500000"); // 3tr5
            default:
                throw new IllegalArgumentException("Thời hạn VIP không hợp lệ");
        }
    }

    /**
     * Cập nhật logic duyệt đơn hàng để xử lý cả đơn hàng mua khóa học và đơn hàng VIP
     */

//Duyệt đơn hàng
// src/main/java/com/example/infinityweb_be/service/order/OrderService.java
    @Transactional
    public void approveOrder(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Chỉ duyệt đơn ở trạng thái PENDING");
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        // Kiểm tra loại đơn hàng và thực hiện logic tương ứng
        if ("Kích hoạt tài khoản VIP".equals(order.getOrderDetails().get(0).getServiceName())) {
            // Logic cho đơn hàng VIP
            User user = order.getUser();

            // Cập nhật trạng thái VIP của user và thời gian hết hạn
            user.setIsVip(true);
            user.setVipExpiryDate(LocalDateTime.now().plusMonths(
                    order.getOrderDetails().get(0).getServiceDesc().contains("12") ? 12 :
                            order.getOrderDetails().get(0).getServiceDesc().contains("6") ? 6 :
                                    order.getOrderDetails().get(0).getServiceDesc().contains("3") ? 3 : 1
            ));
            userRepository.save(user);

            // ✅ Bắt đầu logic mới: Ghi danh người dùng vào tất cả các khóa học
            List<Course> allCourses = courseRepository.findAll();

            for (Course course : allCourses) {
                // Kiểm tra xem người dùng đã được ghi danh vào khóa học này chưa
                if (!enrollmentRepository.existsByUserIdAndCourseId(user.getId(), course.getId())) {
                    enrollmentService.createEnrollment(user, course);
                }
            }

        } else {
            // Logic cũ cho đơn hàng mua khóa học
            Course course = order.getOrderDetails().get(0).getCourse();
            enrollmentService.createEnrollment(order.getUser(), course);
        }
    }



// Xoá đơn hàng
    public void deleteOrderByCode(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }




}
