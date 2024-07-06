package com.rlabs.order_service.web.controller;

import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rlabs.order_service.domain.InvalidOrderException;
import com.rlabs.order_service.domain.OrderService;
import com.rlabs.order_service.domain.SecurityService;
import com.rlabs.order_service.domain.models.CreateOrderRequest;
import com.rlabs.order_service.testdata.TestDataFactory;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(OrderController.class)
public class OrderControllerJUnitTest {
    @MockBean
    private OrderService orderService;

    @MockBean
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        given(securityService.getLoginUserName()).willReturn("rap");
    }

    @ParameterizedTest(name = "[{index}]-{0}")
    @MethodSource("createOrderRequestProvider")
    void shouldReturnBadRequestWhenOrderPayloadIsInvalid(CreateOrderRequest request) throws Exception {
        given(orderService.createOrder(eq("rap"), any(CreateOrderRequest.class)))
                .willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest(name = "[{index}]-{0}")
    @MethodSource("createOrderRequestProviderForInvalid")
    void shouldReturnBadRequestWhenProductIsInvalid(CreateOrderRequest request) throws Exception {
        given(orderService.createOrder(eq("rap"), any(CreateOrderRequest.class)))
                .willThrow(new InvalidOrderException("Price Mismatch"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> createOrderRequestProvider() {
        return Stream.of(
                arguments(
                        named("Order with Invalid Customer", TestDataFactory.createOrderRequestWithInvalidCustomer())),
                arguments(named(
                        "Order with Invalid Delivery Address",
                        TestDataFactory.createOrderRequestWithInvalidDeliveryAddress())),
                arguments(named("Order with No Items", TestDataFactory.createOrderRequestWithNoItems())));
    }

    static Stream<Arguments> createOrderRequestProviderForInvalid() {
        return Stream.of(
                arguments(named(
                        "Order with Invalid Product Id", TestDataFactory.createOrderRequestWithInvalidProductId())),
                arguments(named("Order with Invalid Price", TestDataFactory.createOrderRequestWithInvalidPrice())));
    }
}
