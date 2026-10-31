package com.organicharvest.backend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String customerName;
    private String phone;

    @Column(length = 500)
    private String address;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    // 🔥 NAYE FIELDS YAHAN ADD KIYE HAIN
    private String paymentId;
    private String razorpayOrderId;
    private String paymentStatus;

    public Order() {}

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = OrderStatus.PENDING;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {
        private Order order = new Order();
        public OrderBuilder orderNumber(String orderNumber) { order.setOrderNumber(orderNumber); return this; }
        public OrderBuilder user(User user) { order.setUser(user); return this; }
        public OrderBuilder customerName(String customerName) { order.setCustomerName(customerName); return this; }
        public OrderBuilder phone(String phone) { order.setPhone(phone); return this; }
        public OrderBuilder address(String address) { order.setAddress(address); return this; }
        public OrderBuilder totalAmount(Double totalAmount) { order.setTotalAmount(totalAmount); return this; }
        public OrderBuilder status(OrderStatus status) { order.setStatus(status); return this; }
        public OrderBuilder items(List<OrderItem> items) { order.setItems(items); return this; }
        
        // 🔥 BUILDER MEIN NAYE METHODS ADD KIYE HAIN
        public OrderBuilder paymentId(String paymentId) { order.setPaymentId(paymentId); return this; }
        public OrderBuilder razorpayOrderId(String razorpayOrderId) { order.setRazorpayOrderId(razorpayOrderId); return this; }
        public OrderBuilder paymentStatus(String paymentStatus) { order.setPaymentStatus(paymentStatus); return this; }
        
        public Order build() { return order; }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
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
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    // 🔥 NAYE FIELDS KE GETTERS AUR SETTERS
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}