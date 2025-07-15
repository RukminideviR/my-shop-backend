package com.mouritech.security_frontend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seller")
public class SellerController {

    @PostMapping("/dashboard")
    public ResponseEntity<String> accessSellerDashboard() {
        return ResponseEntity.ok("Hello from Seller Dashboard!");
    }
}

