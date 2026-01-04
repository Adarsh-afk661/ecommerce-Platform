package com.gocart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HealthController {
    
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "GoCart Backend is running!");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "0.1.0");
        return response;
    }
    
    @GetMapping
    public Map<String, Object> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to GoCart API");
        response.put("version", "0.1.0");
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("health", "/api/health");
        endpoints.put("users", "/api/users");
        endpoints.put("products", "/api/products");
        endpoints.put("orders", "/api/orders");
        endpoints.put("stores", "/api/stores");
        endpoints.put("ratings", "/api/ratings");
        endpoints.put("coupons", "/api/coupons");
        endpoints.put("addresses", "/api/addresses");
        response.put("endpoints", endpoints);
        return response;
    }
}
