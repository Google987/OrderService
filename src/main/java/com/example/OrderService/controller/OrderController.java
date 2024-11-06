package com.example.OrderService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.Product;
import com.example.OrderService.service.OrderService;
import com.example.OrderService.service.ProductService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;


    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Order Service!";
    }

    @PostMapping("/")
    public Order addOrder(@RequestBody OrderRequest orderRequest) {
        // Fetch product by ID
        Product product = productService.findProductById(orderRequest.getProductId());

        // Check if the product exists
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + orderRequest.getProductId());
        }

        // Check product quantity availability
        if (product.getQuantity() < orderRequest.getQuantity()) {
            throw new IllegalArgumentException("Insufficient product quantity available");
        }

        // Calculate the price based on quantity
        double price = product.getPrice() * orderRequest.getQuantity();

        // Create a new order object
        Order order = new Order(product, orderRequest.getQuantity(), price, "NEW");

        // Save the order
        return orderService.addOrder(order);
    }
}
