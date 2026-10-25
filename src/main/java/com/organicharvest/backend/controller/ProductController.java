package com.organicharvest.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.organicharvest.backend.entity.Product;
import com.organicharvest.backend.service.ProductService; 

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") 
public class ProductController {

    private final ProductService productService;

    // 🆕 Admin ki Secret Key (Isko kisi ko na batayein)
    private static final String ADMIN_SECRET_KEY = "OrganicHarvestAdmin123";

    // Constructor Injection
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
    public ResponseEntity<?> addProduct(
            @RequestBody Product product,
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {
        
        // Security Check
        if (!ADMIN_SECRET_KEY.equals(adminKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized! Admin access required."));
        }

        return ResponseEntity.ok(productService.addProduct(product));
    }

    // 3. Product update karne ke liye API (Sirf Admin)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id, 
            @RequestBody Product product,
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {
        
        // Security Check
        if (!ADMIN_SECRET_KEY.equals(adminKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized! Admin access required."));
        }

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
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {
        
        // Security Check
        if (!ADMIN_SECRET_KEY.equals(adminKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized! Admin access required."));
        }

        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}