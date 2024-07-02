package com.rlabs.catalog_service;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.rlabs.catalog_service.domain.Product;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
class CatalogServiceApplicationTests extends AbstractIT {

    @Test
    void shouldReturnProducts() {
        given().contentType(ContentType.JSON)
                .when()
                .get("api/products")
                .then()
                .statusCode(200)
                .body("data", hasSize(5))
                .body("totalElements", is(16))
                .body("pageNumber", is(1))
                .body("totalPages", is(4))
                .body("isFirst", is(true))
                .body("isLast", is(false))
                .body("hasNext", is(true))
                .body("hasPrevious", is(false));
    }

    @Test
    void shouldReturnProductsOnPage2() {
        given().contentType(ContentType.JSON)
                .when()
                .get("api/products?page=2")
                .then()
                .statusCode(200)
                .body("data", hasSize(5))
                .body("totalElements", is(16))
                .body("pageNumber", is(2))
                .body("totalPages", is(4))
                .body("isFirst", is(false))
                .body("isLast", is(false))
                .body("hasNext", is(true))
                .body("hasPrevious", is(true));
    }

    @Test
    void shouldReturnProductByCode() {
        Product product = given().contentType(ContentType.JSON)
                .when()
                .get("api/products/{code}", "P100")
                .then()
                .statusCode(200)
                .assertThat()
                .extract()
                .body()
                .as(Product.class);
        assertThat(product.code()).isEqualTo("P100");
        assertThat(product.name()).isEqualTo("The Hunger Games");
        assertThat(product.description()).isEqualTo("Winning will make you famous. Losing means certain death...");
        assertThat(product.price()).isEqualTo(new BigDecimal("34.0"));
    }

    @Test
    void shouldReturnProductNotFoundByCode() {
        var code = "invalid";
        given().contentType(ContentType.JSON)
                .when()
                .get("api/products/{code}", code)
                .then()
                .statusCode(404)
                .body("status", is(404))
                .body("title", is("Product Not Found"))
                .body("detail", is("Product with code " + code + " not found"));
    }
}
