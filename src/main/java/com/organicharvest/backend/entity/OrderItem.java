package com.organicharvest.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private Double subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    public OrderItem() {}

    // Builder
    public static OrderItemBuilder builder() {
        return new OrderItemBuilder();
    }

    public static class OrderItemBuilder {
        private OrderItem item = new OrderItem();
        public OrderItemBuilder productId(Long productId) { item.setProductId(productId); return this; }
        public OrderItemBuilder productName(String productName) { item.setProductName(productName); return this; }
        public OrderItemBuilder price(Double price) { item.setPrice(price); return this; }
        public OrderItemBuilder quantity(Integer quantity) { item.setQuantity(quantity); return this; }
        public OrderItemBuilder subtotal(Double subtotal) { item.setSubtotal(subtotal); return this; }
        public OrderItemBuilder order(Order order) { item.setOrder(order); return this; }
        public OrderItem build() { return item; }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}