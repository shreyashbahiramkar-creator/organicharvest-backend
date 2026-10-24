package com.organicharvest.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.organicharvest.backend.entity.Product;
import com.organicharvest.backend.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Constructor Injection
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Saare products laane ke liye
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Naya product add karne ke liye
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Product delete karne ke liye
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // Product update karne ke liye
    public Product updateProduct(Long id, Product updatedProduct) {
        // Pehle check karo ki product exist karta hai ya nahi
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Purane product mein naye values set karo
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setUnit(updatedProduct.getUnit());
        existingProduct.setImage(updatedProduct.getImage());
        existingProduct.setTag(updatedProduct.getTag());
        
        // 🆕 YEH LINE ADD KAREIN: Stock update karne ke liye
        existingProduct.setStock(updatedProduct.getStock());

        // Save karke return kar do
        return productRepository.save(existingProduct);
    }
}