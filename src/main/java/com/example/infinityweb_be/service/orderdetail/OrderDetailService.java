package com.example.infinityweb_be.service.orderdetail;

import com.example.infinityweb_be.domain.dto.order.OrderDetailDTO;
import com.example.infinityweb_be.repository.order.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public List<OrderDetailDTO> getOrderDetailsByOrderCode(String orderCode) {
        return orderDetailRepository.findByOrder_OrderCode(orderCode)
                .stream()
                .map(detail -> new OrderDetailDTO(
                        detail.getServiceName(),
                        detail.getServiceDesc(),
                        detail.getPrice()
                ))
                .toList();
    }
}
