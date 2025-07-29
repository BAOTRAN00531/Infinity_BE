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

    @Query("""
        SELECT COUNT(o) > 0 FROM Order o
        WHERE o.user.id = :userId
          AND o.course.id = :courseId
          AND o.status = 'PAID'
          AND o.expiryDate >= CURRENT_DATE
    """)
    boolean hasValidOrderByUserId(@Param("userId") Integer userId,
                                  @Param("courseId") Integer courseId);

    @Query("""
        SELECT COUNT(o) > 0 FROM Order o
        WHERE o.user.username = :username
          AND o.course.id = :courseId
          AND o.status = 'PAID'
          AND o.expiryDate >= CURRENT_DATE
    """)
    boolean hasValidOrder(@Param("username") String username,
                          @Param("courseId") Integer courseId);

    boolean existsByUserIdAndCourseIdAndStatus(Integer userId, Integer courseId, OrderStatus status);

    List<Order> findAllByUserId(Integer userId);

}





//trash
//
//boolean hasValidOrderByUserId(Integer userId, Integer courseId);

//boolean existsByUserIdAndCourseIdAndStatus(Integer userId, Integer courseId, OrderStatus status);
