package com.example.OrderService.service;
import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.Product;
import com.example.OrderService.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order processOrderRequest(OrderRequest orderRequest) {
        // Fetch product by ID
        if (orderRequest.getQuantity() < 1) {
            throw new IllegalArgumentException("Product quantity must be greater than zero");
        }

        // Reduce product quantity
        Product product = productService.reduceProductQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        // Calculate the price based on quantity
        double price = product.getPrice() * orderRequest.getQuantity();
        
        // Create a new order object
        Order order = new Order(product, orderRequest.getQuantity(), price, "NEW");

        // Save the order
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        order.setStatus(status);
        
        //TODO (later): implement logic for sending notification to the user
        
        return orderRepository.save(order);
    }
}
