package com.rlabs.order_service.domain;

import com.rlabs.order_service.domain.models.OrderStatus;
import com.rlabs.order_service.domain.models.OrderSummary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(OrderStatus status);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus status) {
        OrderEntity order = this.findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(status);
        this.save(order);
    }

    List<OrderSummary> findByUserName(String userName);

    @Query(
            """
                    select distinct o
                    from OrderEntity o left join fetch o.items
                    where o.userName=:userName and o.orderNumber=:orderNumber
                    """)
    Optional<OrderEntity> findByUserNameAndOrderNumber(String userName, String orderNumber);
}
