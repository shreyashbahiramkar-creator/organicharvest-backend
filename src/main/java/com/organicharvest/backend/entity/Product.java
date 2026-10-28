package com.organicharvest.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private Double price;
    
    // 🆕 Naya Field: Original Price (Discount dikhane ke liye)
    private Double originalPrice; 
    
    private String unit;
    private String image;
    private String tag;
    
    // Stock Field
    private Integer stock; 

    // 1. Khali Constructor (Spring Boot ke liye zaroori hai)
    public Product() {
    }

    // 2. Saare fields wala Constructor (originalPrice ke saath update kiya)
    public Product(Long id, String name, String category, Double price, Double originalPrice, String unit, String image, String tag, Integer stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.originalPrice = originalPrice;
        this.unit = unit;
        this.image = image;
        this.tag = tag;
        this.stock = stock;
    }

    // 3. Getters aur Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    // 🆕 Original Price ke liye Getter aur Setter
    public Double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}