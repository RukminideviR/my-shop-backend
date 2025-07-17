package com.mouritech.security_frontend.maincontroller;

import com.mouritech.security_frontend.analyticsDto.AdminAnalyticsDto;
import com.mouritech.security_frontend.mainservice.AdminAnalyticsService;
import com.mouritech.security_frontend.mainservice.OrderService;
import com.mouritech.security_frontend.orderdto.OrderResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminAnalyticsController {

    @Autowired
    private AdminAnalyticsService analyticsService;
    
    @Autowired
    private OrderService orderService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<AdminAnalyticsDto> getAnalytics() {
        return ResponseEntity.ok(analyticsService.getAnalytics());
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    
}