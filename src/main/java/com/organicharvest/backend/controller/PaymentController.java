package com.organicharvest.backend.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> data) {
        try {
            // Amount ko paise me convert karein (Razorpay paise me leta hai)
            int amount = (int) (Double.parseDouble(data.get("amount").toString()) * 100);
            
            RazorpayClient client = new RazorpayClient(keyId, keySecret);
            
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = client.orders.create(orderRequest);
            
            // 🆕 Frontend ko sahi JSON bhejein (id, amount, currency aur key)
            Map<String, Object> response = new HashMap<>();
            response.put("id", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("key", keyId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log me error dekhein
            return ResponseEntity.status(500).body(Map.of("error", "Error creating order: " + e.getMessage()));
        }
    }
}