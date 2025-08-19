// src/main/java/com/example/infinityweb_be/domain/OrderDetail.java
package com.example.infinityweb_be.domain;

import com.example.infinityweb_be.domain.course.Course;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Mối quan hệ với Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // ✅ Mối quan hệ với Course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "service_desc")
    private String serviceDesc;

    @Column(name = "price", nullable = false)
    private BigDecimal price;
}