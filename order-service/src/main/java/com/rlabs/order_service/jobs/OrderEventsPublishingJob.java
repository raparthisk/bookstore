package com.rlabs.order_service.jobs;

import com.rlabs.order_service.domain.OrderEventRepository;
import com.rlabs.order_service.domain.OrderEventService;
import java.time.Instant;
import net.javacrumbs.shedlock.core.LockAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsPublishingJob {

    private static final Logger log = LoggerFactory.getLogger(OrderEventRepository.class);
    private final OrderEventService orderEventService;

    OrderEventsPublishingJob(OrderEventService orderEventService) {
        this.orderEventService = orderEventService;
    }

    //    @Scheduled(cron = "${orders.publish-order-events-job-cron}")
    //    @SchedulerLock(name = "publishOrderEvents")
    public void publishOrderEvents() {
        LockAssert.assertLocked();
        log.info("Publish Order events at: " + Instant.now());
        orderEventService.publishOrderEvents();
    }
}
