package com.rlabs.order_service.domain;

import com.rlabs.order_service.ApplicationProperties;
import com.rlabs.order_service.domain.models.OrderCancelledEvent;
import com.rlabs.order_service.domain.models.OrderCreatedEvent;
import com.rlabs.order_service.domain.models.OrderDeliveredEvent;
import com.rlabs.order_service.domain.models.OrderErrorEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;

    OrderEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(OrderCreatedEvent orderCreatedEvent) {
        this.send(applicationProperties.newOrdersQueue(), orderCreatedEvent);
    }

    public void publish(OrderErrorEvent orderErrorEvent) {
        this.send(applicationProperties.errorOrdersQueue(), orderErrorEvent);
    }

    public void publish(OrderDeliveredEvent orderDeliveredEvent) {
        this.send(applicationProperties.deliveredOrdersQueue(), orderDeliveredEvent);
    }

    public void publish(OrderCancelledEvent orderCancelledEvent) {
        this.send(applicationProperties.cancelledOrdersQueue(), orderCancelledEvent);
    }

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(applicationProperties.orderEventsExchange(), routingKey, payload);
    }
}
