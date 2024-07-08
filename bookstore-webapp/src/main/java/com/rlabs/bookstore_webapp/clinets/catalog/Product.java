package com.rlabs.bookstore_webapp.clinets.catalog;

import java.math.BigDecimal;

public record Product(Long id, String code, String name, String description, String imageURL, BigDecimal price) {}
