package com.rlabs.order_service.domain;

import com.rlabs.order_service.clients.catalog.Product;
import com.rlabs.order_service.clients.catalog.ProductServiceClient;
import com.rlabs.order_service.domain.models.CreateOrderRequest;
import com.rlabs.order_service.domain.models.OrderItem;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private static final Logger log = LoggerFactory.getLogger(OrderValidator.class);
    private final ProductServiceClient productServiceClient;

    OrderValidator(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    void validate(CreateOrderRequest request) {
        Set<OrderItem> items = request.items();
        for (OrderItem item : items) {
            Product product = productServiceClient
                    .getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid Product Code: " + item.code()));
            if (item.price().compareTo(product.price()) != 0) {
                log.error("Product price mismatch");
                throw new InvalidOrderException("Product price not matching");
            }
        }
    }
}
