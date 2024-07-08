package com.rlabs.bookstore_webapp.controllers;

import com.rlabs.bookstore_webapp.clinets.orders.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(OrderController.class);
    private  final OrderServiceClient orderServiceClient;
    OrderController(OrderServiceClient orderServiceClient){
        this.orderServiceClient = orderServiceClient;
    }

    @GetMapping("/cart")
    public String cart(){
        return "cart";
    }

    @GetMapping("/orders/{orderNumber}")
    public String order(@PathVariable String orderNumber, Model model){
        model.addAttribute("orderNumber", orderNumber);
        return "order_details";
    }


    @GetMapping("/api/orders/{orderNumber}")
    @ResponseBody
    OrderDTO getOrder(@PathVariable String orderNumber) {
        log.info("Fetching order details for orderNumber: {}", orderNumber);
        return orderServiceClient.getOrder(orderNumber);
    }

    @GetMapping("/orders")
    public String orders(){
        return "orders";
    }

    @GetMapping("/api/orders")
    @ResponseBody
    List<OrderSummary> getOrders() {
        log.info("Fetching orders");
        return orderServiceClient.getOrders();
    }

    @PostMapping("/api/orders")
    @ResponseBody
    OrderConfirmationDTO createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        log.info("Creating order: {}", orderRequest);
        return orderServiceClient.createOrder(orderRequest);
    }
}
