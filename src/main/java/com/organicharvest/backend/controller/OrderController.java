package com.organicharvest.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.organicharvest.backend.dto.OrderRequest;
import com.organicharvest.backend.dto.OrderResponse;
import com.organicharvest.backend.entity.OrderStatus;
import com.organicharvest.backend.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.placeOrder(request));
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    // 🆕 NAYA ENDPOINT: Order Number se track karne ke liye
    @GetMapping("/track/{orderNumber}")
    public ResponseEntity<OrderResponse> trackOrder(@PathVariable String orderNumber) {
        try {
            return ResponseEntity.ok(orderService.getOrderByNumber(orderNumber));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Admin ke liye status update karne ke liye
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id, 
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}