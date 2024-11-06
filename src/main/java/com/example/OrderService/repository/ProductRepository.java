package com.example.OrderService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.OrderService.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // No additional methods are needed; CRUD methods are inherited from JpaRepository
}
