package com.example.infinityweb_be.repository.order;

import com.example.infinityweb_be.domain.OrderDetail;
import io.micrometer.common.KeyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder_OrderCode(String orderCode); // Tìm theo orderCode
}