package com.organicharvest.backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.organicharvest.backend.dto.OrderRequest;
import com.organicharvest.backend.dto.OrderResponse;
import com.organicharvest.backend.entity.Order;
import com.organicharvest.backend.entity.OrderItem;
import com.organicharvest.backend.entity.OrderStatus;
import com.organicharvest.backend.entity.Product; // <-- Zaroori Import
import com.organicharvest.backend.repository.OrderRepository;
import com.organicharvest.backend.repository.ProductRepository; // <-- Zaroori Import

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository; // <-- Naya Inject

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        double total = 0.0;

        // 🆕 Step 1: Pehle check karo ki stock available hai ya nahi
        for (var itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));

            // Agar stock null hai ya quantity se kam hai, toh error throw karo
            if (product.getStock() == null || product.getStock() < itemReq.getQuantity()) {
                throw new RuntimeException("Out of stock! Only " + (product.getStock() == null ? 0 : product.getStock()) + " left for " + product.getName());
            }
            total += itemReq.getPrice() * itemReq.getQuantity();
        }

        // Step 2: Order Build karo
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .customerName(request.getCustomerName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .totalAmount(total)
                .status(OrderStatus.PENDING)
                .build();

        // Step 3: Items build karo aur Stock kam karo
        List<OrderItem> items = request.getItems().stream()
                .map(i -> {
                    // Stock deduct karo
                    Product product = productRepository.findById(i.getProductId()).get();
                    product.setStock(product.getStock() - i.getQuantity());
                    productRepository.save(product); // DB mein update karo

                    return OrderItem.builder()
                            .productId(i.getProductId())
                            .productName(i.getProductName())
                            .price(i.getPrice())
                            .quantity(i.getQuantity())
                            .subtotal(i.getPrice() * i.getQuantity())
                            .order(order)
                            .build();
                })
                .collect(Collectors.toList());

        order.setItems(items);
        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public OrderResponse getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found with number: " + orderNumber));
        return toResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        
        return toResponse(updatedOrder);
    }

    private String generateOrderNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = orderRepository.count() + 1;
        return String.format("ORD-%s-%04d", date, count);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderResponse.Item> items = order.getItems().stream()
                .map(i -> OrderResponse.Item.builder()
                        .productId(i.getProductId())
                        .productName(i.getProductName())
                        .price(i.getPrice())
                        .quantity(i.getQuantity())
                        .subtotal(i.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerName(order.getCustomerName())
                .phone(order.getPhone())
                .address(order.getAddress())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }
}