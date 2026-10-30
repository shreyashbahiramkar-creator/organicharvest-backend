package com.organicharvest.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.organicharvest.backend.entity.Product;
import com.organicharvest.backend.service.ProductService; 

@RestController
@RequestMapping("/api/products")
// 🆕 @CrossOrigin hata diya kyunki SecurityConfig mein globally set hai
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. Saare products fetch karne ke liye API (Sabke liye open)
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 2. Naya product add karne ke liye API (Sirf Admin)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // 🆕 Security yahan lag gayi
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    // 3. Product update karne ke liye API (Sirf Admin)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // 🆕 Security yahan lag gayi
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id, 
            @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // 4. Product Delete karne ke liye API (Sirf Admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // 🆕 Security yahan lag gayi
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // 5. Bulk Products Add karne ke liye API (Sirf Admin)
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')") // 🆕 Security yahan lag gayi
    public ResponseEntity<?> addBulkProducts(@RequestBody List<Product> products) {
        try {
            for (Product product : products) {
                productService.addProduct(product);
            }
            return ResponseEntity.ok(Map.of("message", products.size() + " Products added successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error adding bulk products: " + e.getMessage()));
        }
    }
}