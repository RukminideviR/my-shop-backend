package com.mouritech.security_frontend.mainserviceImpl;

import com.mouritech.security_frontend.analyticsDto.SellerAnalyticsDto;
import com.mouritech.security_frontend.mainentity.Order;
import com.mouritech.security_frontend.mainentity.OrderItem;
import com.mouritech.security_frontend.mainentity.Product;
import com.mouritech.security_frontend.mainservice.SellerAnalyticsService;
import com.mouritech.security_frontend.orderdto.OrderItemResponse;
import com.mouritech.security_frontend.orderdto.OrderResponse;
import com.mouritech.security_frontend.repository.ProductRepository;
import com.mouritech.security_frontend.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<OrderResponse> getOrdersBySellerEmail(String sellerEmail) {
        List<Order> allOrders = orderRepository.findAll();

        return allOrders.stream()
            .filter(order -> order.getItems().stream()
                .anyMatch(item -> {
                    Product product = item.getProduct();
                    return product != null &&
                           sellerEmail.equals(product.getCreatedBy());  // ✅ FIXED: using createdBy
                }))
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderDate(order.getOrderDate());
        response.setShippingAddress(order.getShippingAddress());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponse> itemResponses = order.getItems().stream()
            .map(item -> {
                OrderItemResponse itemResponse = new OrderItemResponse();
                itemResponse.setProductName(item.getProduct().getProductName());
                itemResponse.setQuantity(item.getQuantity());
                itemResponse.setPrice(item.getPrice());
                itemResponse.setStatus(item.getStatus());
                return itemResponse;
            }).collect(Collectors.toList());

        response.setItems(itemResponses);
        return response;
    }

}
