package com.organicharvest.backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
    private String customerName;
    private String phone;
    private String address;
    private List<OrderItemRequest> items;
}