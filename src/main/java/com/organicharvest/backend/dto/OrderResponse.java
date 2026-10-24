package com.organicharvest.backend.dto;

import com.organicharvest.backend.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
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

    @Data
    @Builder
    public static class Item {
        private Long productId;
        private String productName;
        private Double price;
        private Integer quantity;
        private Double subtotal;
    }
}