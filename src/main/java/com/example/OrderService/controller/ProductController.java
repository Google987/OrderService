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
        // Ensure the product name is not empty or null
        if (productRequest.getName() == null || productRequest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }

        // Create a new Product object from the request data
        Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getQuantity()
        );

        // Save the product using the service
        Product savedProduct = productService.addProduct(product);

        // Return the saved product with a 201 Created status
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

}
