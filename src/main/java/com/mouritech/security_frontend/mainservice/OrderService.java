package com.mouritech.security_frontend.mainservice;

import com.mouritech.security_frontend.orderdto.OrderRequest;
import com.mouritech.security_frontend.orderdto.OrderResponse;
import java.util.List;

public interface OrderService {
   // OrderResponse placeOrder(OrderRequest request, String userEmail);
    OrderResponse placeOrder(OrderRequest request, String userEmail);
    List<OrderResponse> getOrders(String userEmail);
    void cancelOrderItem(Long orderId, Long itemId, int quantity, String userEmail);
    void returnOrderItem(Long orderId, Long itemId, int quantity, String userEmail);
	List<OrderResponse> getAllOrders();

}