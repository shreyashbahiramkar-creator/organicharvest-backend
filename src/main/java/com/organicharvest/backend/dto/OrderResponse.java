package com.organicharvest.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.organicharvest.backend.entity.OrderStatus;

public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String customerName;
    private String phone;
    private String address;
    private Double totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<Item> items;

    public OrderResponse() {}

    // Builder
    public static OrderResponseBuilder builder() {
        return new OrderResponseBuilder();
    }

    public static class OrderResponseBuilder {
        private OrderResponse res = new OrderResponse();
        public OrderResponseBuilder id(Long id) { res.setId(id); return this; }
        public OrderResponseBuilder orderNumber(String orderNumber) { res.setOrderNumber(orderNumber); return this; }
        public OrderResponseBuilder customerName(String customerName) { res.setCustomerName(customerName); return this; }
        public OrderResponseBuilder phone(String phone) { res.setPhone(phone); return this; }
        public OrderResponseBuilder address(String address) { res.setAddress(address); return this; }
        public OrderResponseBuilder totalAmount(Double totalAmount) { res.setTotalAmount(totalAmount); return this; }
        public OrderResponseBuilder status(OrderStatus status) { res.setStatus(status); return this; }
        public OrderResponseBuilder createdAt(LocalDateTime createdAt) { res.setCreatedAt(createdAt); return this; }
        public OrderResponseBuilder items(List<Item> items) { res.setItems(items); return this; }
        public OrderResponse build() { return res; }
    }

    public static class Item {
        private Long productId;
        private String productName;
        private String productImage; // 🔥 YEH NAYI LINE ADD KI HAI
        private Double price;
        private Integer quantity;
        private Double subtotal;

        public Item() {}

        public static ItemBuilder builder() {
            return new ItemBuilder();
        }

        public static class ItemBuilder {
            private Item item = new Item();
            public ItemBuilder productId(Long productId) { item.setProductId(productId); return this; }
            public ItemBuilder productName(String productName) { item.setProductName(productName); return this; }
            public ItemBuilder productImage(String productImage) { item.setProductImage(productImage); return this; } // 🔥 YEH NAYI LINE
            public ItemBuilder price(Double price) { item.setPrice(price); return this; }
            public ItemBuilder quantity(Integer quantity) { item.setQuantity(quantity); return this; }
            public ItemBuilder subtotal(Double subtotal) { item.setSubtotal(subtotal); return this; }
            public Item build() { return item; }
        }

        // Item Getters & Setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        // 🔥 YEH NAYE GETTER AUR SETTER ADD KIYE HAIN
        public String getProductImage() { return productImage; }
        public void setProductImage(String productImage) { this.productImage = productImage; }
        
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    }

    // OrderResponse Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}