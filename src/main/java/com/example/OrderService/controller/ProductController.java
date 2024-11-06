package com.example.OrderService.controller;

import com.example.OrderService.dto.ProductRequest;
import com.example.OrderService.model.Product;
import com.example.OrderService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Product Service!";
    }

    @PostMapping("/")
    public ResponseEntity<Product> addProduct(@RequestBody ProductRequest productRequest) {

        // Save the product using the service
        Product savedProduct = productService.addProduct(productRequest);

        // Return the saved product with a 201 Created status
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

}
