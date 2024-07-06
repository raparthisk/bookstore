package com.rlabs.order_service.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rlabs.order_service.domain.models.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderEventService {
    private OrderEventRepository orderEventRepository;
    private ObjectMapper objectMapper;
    private OrderEventPublisher orderEventPublisher;
    private static final Logger log = LoggerFactory.getLogger(OrderEventService.class);

    OrderEventService(
            OrderEventRepository orderEventRepository,
            ObjectMapper objectMapper,
            OrderEventPublisher orderEventPublisher) {
        this.orderEventRepository = orderEventRepository;
        this.objectMapper = objectMapper;
        this.orderEventPublisher = orderEventPublisher;
    }

    void save(OrderCreatedEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CREATED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    void save(OrderDeliveredEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_DELIVERED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    void save(OrderCancelledEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CANCELLED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    void save(OrderErrorEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_PROCESSING_FAILED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    private String toJsonPayload(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJsonPayload(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        List<OrderEventEntity> orderEvents = orderEventRepository.findAll(sort);
        for (OrderEventEntity event : orderEvents) {
            this.publishEvent(event);
            orderEventRepository.delete(event);
        }
    }

    public void publishEvent(OrderEventEntity event) {
        OrderEventType eventType = event.getEventType();
        switch (eventType) {
            case ORDER_CREATED:
                OrderCreatedEvent orderCreatedEvent = this.fromJsonPayload(event.getPayload(), OrderCreatedEvent.class);
                orderEventPublisher.publish(orderCreatedEvent);
                break;
            case ORDER_CANCELLED:
                OrderCancelledEvent orderCancelledEvent =
                        this.fromJsonPayload(event.getPayload(), OrderCancelledEvent.class);
                orderEventPublisher.publish(orderCancelledEvent);
                break;
            case ORDER_DELIVERED:
                OrderDeliveredEvent orderDeliveredEvent =
                        this.fromJsonPayload(event.getPayload(), OrderDeliveredEvent.class);
                orderEventPublisher.publish(orderDeliveredEvent);
                break;
            case ORDER_PROCESSING_FAILED:
                OrderErrorEvent orderErrorEvent = this.fromJsonPayload(event.getPayload(), OrderErrorEvent.class);
                orderEventPublisher.publish(orderErrorEvent);
                break;
            default:
                log.warn("Unsupported OrderEventType: {}", eventType);
        }
    }
}
