package com.rlabs.order_service.domain.models;

import jakarta.validation.constraints.NotBlank;

public record Address(
        @NotBlank(message = "AddressLine1 is required") String addressLine1,
        String addressLine2,
        @NotBlank(message = "city is required") String city,
        @NotBlank(message = "state is required") String state,
        @NotBlank(message = "zipCode is required") String zipCode,
        @NotBlank(message = "country is required") String country) {}
