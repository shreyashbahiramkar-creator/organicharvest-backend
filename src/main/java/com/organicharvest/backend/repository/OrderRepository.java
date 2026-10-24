package com.organicharvest.backend.repository;

import java.util.Optional; // <-- Yeh import zaroori hai

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.organicharvest.backend.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // 🆕 Naya method: Order Number se order dhoondhne ke liye
    Optional<Order> findByOrderNumber(String orderNumber); 
    
}