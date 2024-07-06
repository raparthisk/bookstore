package com.rlabs.notification_service.domain.models;

import com.rlabs.notification_service.domain.OrderEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEventRepository extends JpaRepository<OrderEventEntity, Long> {
    boolean existsByEventId(String eventId);
}
