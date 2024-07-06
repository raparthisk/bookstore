package com.rlabs.order_service.web.controller;

import com.rlabs.order_service.domain.OrderNotFoundException;
import com.rlabs.order_service.domain.OrderService;
import com.rlabs.order_service.domain.SecurityService;
import com.rlabs.order_service.domain.models.CreateOrderRequest;
import com.rlabs.order_service.domain.models.CreateOrderResponse;
import com.rlabs.order_service.domain.models.OrderDTO;
import com.rlabs.order_service.domain.models.OrderSummary;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final SecurityService securityService;

    OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        String userName = securityService.getLoginUserName();
        return orderService.createOrder(userName, request);
    }

    @GetMapping
    List<OrderSummary> getOrders() {
        String userName = securityService.getLoginUserName();
        return orderService.findOrders(userName);
    }

    @GetMapping("/{orderId}")
    OrderDTO getOrder(@PathVariable String orderId) {
        String userName = securityService.getLoginUserName();
        return orderService.findUserOrders(userName, orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
