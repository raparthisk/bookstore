package com.rlabs.bookstore_webapp.controllers;

import com.rlabs.bookstore_webapp.clinets.catalog.CatalogServiceClient;
import com.rlabs.bookstore_webapp.clinets.catalog.PagedResult;
import com.rlabs.bookstore_webapp.clinets.catalog.Product;
import com.rlabs.bookstore_webapp.clinets.orders.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);
    private CatalogServiceClient catalogServiceClient;

    ProductController(CatalogServiceClient catalogServiceClient){
        this.catalogServiceClient = catalogServiceClient;
    }
    @GetMapping
    public String index(){
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String productsPage(@RequestParam(value = "page", defaultValue = "1") int pageNo, Model model){
        model.addAttribute("pageNo", pageNo);
        return "/products";
    }

    @GetMapping("/api/products")
    @ResponseBody
    PagedResult<Product> products(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        log.info("Fetching products for page: {}", page);
        return catalogServiceClient.getProducts(page);
    }

}
