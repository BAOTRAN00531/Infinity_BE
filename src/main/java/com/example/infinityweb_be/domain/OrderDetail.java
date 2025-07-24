package com.example.infinityweb_be.domain;

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

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "service_desc")
    private String serviceDesc;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

}
