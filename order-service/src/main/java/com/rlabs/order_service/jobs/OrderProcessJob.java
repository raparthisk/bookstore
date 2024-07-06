package com.rlabs.order_service.jobs;

import com.rlabs.order_service.domain.OrderEventRepository;
import com.rlabs.order_service.domain.OrderService;
import java.time.Instant;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessJob {
    private static final Logger log = LoggerFactory.getLogger(OrderEventRepository.class);
    private final OrderService orderService;

    OrderProcessJob(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "${orders.process-new-order-job-cron}")
    @SchedulerLock(name = "processNewOrder")
    public void processNewOrder() {
        LockAssert.assertLocked();
        log.info("Process New Order  at: " + Instant.now());
        orderService.publishNewOrder();
    }
}
