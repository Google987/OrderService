package com.example.OrderService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.model.Order;
import com.example.OrderService.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Order Service!";
    }

    @PostMapping("/")
    public Order addOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.processOrderRequest(orderRequest);
    }

    @PutMapping("/{orderId}/status")
    public Order updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, status);
    }
}
