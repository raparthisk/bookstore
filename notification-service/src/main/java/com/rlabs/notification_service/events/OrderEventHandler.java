package com.rlabs.notification_service.events;

import com.rlabs.notification_service.domain.NotificationService;
import com.rlabs.notification_service.domain.OrderEventEntity;
import com.rlabs.notification_service.domain.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private final NotificationService notificationService;
    private final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);
    private final OrderEventRepository orderEventRepository;

    OrderEventHandler(NotificationService notificationService, OrderEventRepository orderEventRepository) {
        this.notificationService = notificationService;
        this.orderEventRepository = orderEventRepository;
    }

    @RabbitListener(queues = "${notification.new-orders-queue}")
    void handlerCreateOrderEvent(OrderCreatedEvent event) {
        log.info("Order Create Event: " + event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.info("Duplicate Order Create Event: " + event);
            return;
        }
        notificationService.sendOrderCreatedNotification(event);
        var entity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(entity);
    }

    @RabbitListener(queues = "${notification.cancelled-orders-queue}")
    void handlerCancelledOrderEvent(OrderCancelledEvent event) {
        log.info("Order Cancelled Event: " + event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.info("Duplicate Order Cancelled Event: " + event);
            return;
        }
        notificationService.sendOrderCancelledNotification(event);
        var entity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(entity);
    }

    @RabbitListener(queues = "${notification.delivered-orders-queue}")
    void handlerDeliveredOrderEvent(OrderDeliveredEvent event) {
        log.info("Order Delivered Event: " + event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.info("Duplicate Order Delivered Event: " + event);
            return;
        }
        notificationService.sendOrderDeliveredNotification(event);
        var entity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(entity);
    }

    @RabbitListener(queues = "${notification.error-orders-queue}")
    void handlerCreateOrderEvent(OrderErrorEvent event) {
        log.info("Order Error Event: " + event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.info("Duplicate Order Error Event: " + event);
            return;
        }
        notificationService.sendOrderErrorEventNotification(event);
        var entity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(entity);
    }
}
