package com.rlabs.order_service.domain;

import com.rlabs.order_service.domain.models.CreateOrderRequest;
import com.rlabs.order_service.domain.models.OrderItem;
import com.rlabs.order_service.domain.models.OrderStatus;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class OrderMapper {
    static OrderEntity convertToEntity(CreateOrderRequest request) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderNumber(UUID.randomUUID().toString());
        orderEntity.setDeliveryAddress(request.deliveryAddress());
        orderEntity.setStatus(OrderStatus.NEW);
        orderEntity.setCustomer(request.customer());
        Set<OrderItemEntity> orderItems = new HashSet<>();
        for (OrderItem item : request.items()) {
            OrderItemEntity order = new OrderItemEntity();
            order.setCode(item.code());
            order.setName(item.name());
            order.setPrice(item.price());
            order.setQuantity(item.quantity());
            order.setOrder(orderEntity);
            orderItems.add(order);
        }
        orderEntity.setItems(orderItems);
        return orderEntity;
    }
}
