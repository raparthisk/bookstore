package com.rlabs.notification_service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification")
public record ApplicationProperties(
        String supportEmail,
        String orderEventsExchange,
        String newOrdersQueue,
        String deliveredOrdersQueue,
        String cancelledOrdersQueue,
        String errorOrdersQueue) {}
