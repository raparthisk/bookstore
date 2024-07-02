package com.rlabs.catalog_service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
        properties = {"spring.test.database.replace=none", "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db"})
@Sql("/test-data.sql")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> productEntityList = productRepository.findAll();
        assertThat(productEntityList).hasSize(16);
    }

    @Test
    void shouldGetProductByCode() {
        ProductEntity product = productRepository.findByCode("P100").orElseThrow();
        assertThat(product.getCode()).isEqualTo("P100");
        assertThat(product.getName()).isEqualTo("The Hunger Games");
        assertThat(product.getImageUrl()).isEqualTo("https://images.gr-assets.com/books/1447303603l/2767052.jpg");
        assertThat(product.getPrice()).isEqualTo("34.0");
    }

    @Test
    void shouldReturnEmptyForInvalidCode() {
        assertThat(productRepository.findByCode("Invalid code")).isEmpty();
    }
}
