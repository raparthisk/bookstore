package com.rlabs.catalog_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
// we can use either one
// @EnableConfigurationProperties(ApplicationProperties.class)
@ConfigurationPropertiesScan
public class CatalogServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(CatalogServiceApplication.class, args);
    }
}
