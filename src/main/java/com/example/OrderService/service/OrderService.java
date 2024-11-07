package com.example.OrderService.service;
import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.OrderStatus;
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
        Order order = new Order(product, orderRequest.getQuantity(), price, OrderStatus.NEW);

        // Save the order
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        // Validate status transition
        validateStatusTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);
        
        //TODO (later): implement logic for sending notification to the user
        
        return orderRepository.save(order);
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot change status of a cancelled order");
        }

        switch (currentStatus) {
            case NEW:
                if (newStatus != OrderStatus.PROCESSING && newStatus != OrderStatus.CANCELLED) {
                    throw new IllegalArgumentException("Invalid status transition from NEW to " + newStatus);
                }
                break;
            case PROCESSING:
                if (newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELLED) {
                    throw new IllegalArgumentException("Invalid status transition from PROCESSING to " + newStatus);
                }
                break;
            case SHIPPED:
                if (newStatus != OrderStatus.DELIVERED) {
                    throw new IllegalArgumentException("Invalid status transition from SHIPPED to " + newStatus);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported status transition");
        }
    }
}
