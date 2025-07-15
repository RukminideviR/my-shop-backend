package com.mouritech.security_frontend.mainserviceImpl;

import com.mouritech.security_frontend.repository.ProductRepository;
import com.mouritech.security_frontend.repository.UserRepository;
import com.mouritech.security_frontend.repository.OrderRepository;
import com.mouritech.security_frontend.analyticsDto.AdminAnalyticsDto;
import com.mouritech.security_frontend.mainservice.AdminAnalyticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public AdminAnalyticsDto getAnalytics() {
        AdminAnalyticsDto dto = new AdminAnalyticsDto();
        dto.setTotalProducts(productRepository.count());
        dto.setTotalUsers(userRepository.count());
        dto.setTotalOrders(orderRepository.count());
        Double totalRevenue = orderRepository.getTotalRevenue();
        dto.setTotalRevenue(totalRevenue != null ? totalRevenue : 0.0);
        return dto;
    }
}