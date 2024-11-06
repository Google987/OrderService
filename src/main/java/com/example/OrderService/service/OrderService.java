package com.example.OrderService.service;
import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.Product;
import com.example.OrderService.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    public Order processOrderRequest(OrderRequest orderRequest) {
        // Fetch product by ID
        if (orderRequest.getQuantity() < 1) {
            throw new IllegalArgumentException("Product quantity must be greater than zero");
        }

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
        return orderRepository.save(order);
    }

}
