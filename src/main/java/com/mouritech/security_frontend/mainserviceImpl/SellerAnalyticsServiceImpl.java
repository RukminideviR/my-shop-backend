package com.mouritech.security_frontend.mainserviceImpl;

import com.mouritech.security_frontend.analyticsDto.SellerAnalyticsDto;
import com.mouritech.security_frontend.mainservice.SellerAnalyticsService;
import com.mouritech.security_frontend.repository.ProductRepository;
import com.mouritech.security_frontend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerAnalyticsServiceImpl implements SellerAnalyticsService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public SellerAnalyticsDto getAnalytics(String sellerEmail) {
        SellerAnalyticsDto dto = new SellerAnalyticsDto();
        dto.setTotalProducts(productRepository.countByCreatedBy(sellerEmail));
        dto.setTotalOrders(orderRepository.countOrdersBySeller(sellerEmail));
        Double totalRevenue = orderRepository.getTotalRevenueBySeller(sellerEmail);
        dto.setTotalRevenue(totalRevenue != null ? totalRevenue : 0.0);
        return dto;
    }
}