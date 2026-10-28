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
import com.organicharvest.backend.entity.Product;
import com.organicharvest.backend.repository.OrderRepository;
import com.organicharvest.backend.repository.ProductRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        double total = 0.0;

        // 🆕 Helper method: ID ko safely parse karne ke liye
        // Agar ID "222-500ml" aayi toh ye "222" nikal kar Long me convert karega
        java.util.function.Function<String, Long> parseProductId = (idStr) -> {
            try {
                String cleanId = idStr.split("-")[0]; // "-" se pehle wala part
                return Long.parseLong(cleanId);
            } catch (Exception e) {
                throw new RuntimeException("Invalid Product ID format: " + idStr);
            }
        };

        // Step 1: Stock check
        for (var itemReq : request.getItems()) {
            Long actualProductId = parseProductId.apply(itemReq.getProductId().toString());
            
            Product product = productRepository.findById(actualProductId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + actualProductId));

            if (product.getStock() == null || product.getStock() < itemReq.getQuantity()) {
                throw new RuntimeException("Out of stock! Only " + (product.getStock() == null ? 0 : product.getStock()) + " left for " + product.getName());
            }
            total += itemReq.getPrice() * itemReq.getQuantity();
        }

        // Step 2: Order Build
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .customerName(request.getCustomerName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .totalAmount(total)
                .status(OrderStatus.PENDING)
                .build();

        // Step 3: Items build aur Stock deduct
        List<OrderItem> items = request.getItems().stream()
                .map(i -> {
                    Long actualProductId = parseProductId.apply(i.getProductId().toString());
                    
                    Product product = productRepository.findById(actualProductId).get();
                    product.setStock(product.getStock() - i.getQuantity());
                    productRepository.save(product);

                    return OrderItem.builder()
                            .productId(actualProductId) // 🆕 Ab yahan sirf 222 save hoga
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