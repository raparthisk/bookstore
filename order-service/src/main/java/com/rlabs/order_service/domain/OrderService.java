package com.rlabs.order_service.domain;

import com.rlabs.order_service.domain.models.CreateOrderRequest;
import com.rlabs.order_service.domain.models.CreateOrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request) {
        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        newOrder.setUserName(userName);
        OrderEntity orderEntity = orderRepository.save(newOrder);
        return new CreateOrderResponse(orderEntity.getOrderNumber());
    }
}
