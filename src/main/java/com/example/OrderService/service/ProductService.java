package com.example.OrderService.service;
import com.example.OrderService.dto.ProductRequest;
import com.example.OrderService.model.Product;
import com.example.OrderService.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product findProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public Product addProduct(ProductRequest productRequest) {
        validateProductRequest(productRequest);
        
        // Create a new Product object from the request data
        Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getQuantity()
        );
        return productRepository.save(product);
    }

    public Product reduceProductQuantity(Long productId, int quantity) {
        Product product = findProductById(productId);
        
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }

        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient product quantity available");
        }

        //TODO (later): if the new product quantity is less than 10, send notification to the seller 

        // Reduce quantity
        product.setQuantity(product.getQuantity() - quantity);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductRequest productRequest) {
    
        validateProductRequest(productRequest);

        // Find the existing product
        Product existingProduct = findProductById(id);
    
        if (existingProduct == null) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
    
        // Update fields
        existingProduct.setName(productRequest.getName());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setQuantity(productRequest.getQuantity());
    
        // Save and return the updated product
        return productRepository.save(existingProduct);
    }

    private void validateProductRequest(ProductRequest productRequest) {
        // Validate the input data
        if (productRequest.getName() == null || productRequest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (productRequest.getQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity cannot be less than zero");
        }
        if (productRequest.getPrice() < 0) {
            throw new IllegalArgumentException("Product price cannot be less than zero");
        }
    }
    
}

