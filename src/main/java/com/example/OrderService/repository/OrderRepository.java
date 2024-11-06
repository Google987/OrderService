package com.example.OrderService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.OrderService.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // No additional methods are needed; CRUD methods are inherited from JpaRepository
}
