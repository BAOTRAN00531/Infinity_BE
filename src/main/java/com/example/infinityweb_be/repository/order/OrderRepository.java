package com.example.infinityweb_be.repository.order;

import com.example.infinityweb_be.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderCode(String orderCode);

    @Query("""
        SELECT COUNT(o) > 0 FROM Order o
        WHERE o.user.username = :username
          AND o.course.id = :courseId
          AND o.status = 'PAID'
          AND o.expiryDate >= CURRENT_DATE
    """)
    boolean hasValidOrder(String username, Integer courseId);
}