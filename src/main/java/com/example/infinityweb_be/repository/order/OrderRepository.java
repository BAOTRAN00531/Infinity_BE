package com.example.infinityweb_be.repository.order;

import com.example.infinityweb_be.domain.Order;
import com.example.infinityweb_be.domain.dto.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderCode(String orderCode);

    // ✅ XÓA phương thức này
    // @Query(...)
    // boolean hasValidOrderByUserId(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    // ✅ XÓA phương thức này
    // @Query(...)
    // boolean hasValidOrder(@Param("username") String username, @Param("courseId") Integer courseId);

    List<Order> findByUserId(Integer userId);
    List<Order> findByUserIdAndStatus(Integer userId, OrderStatus status);
}




//trash
//
//boolean hasValidOrderByUserId(Integer userId, Integer courseId);

//boolean existsByUserIdAndCourseIdAndStatus(Integer userId, Integer courseId, OrderStatus status);
