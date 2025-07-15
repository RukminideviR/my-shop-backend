package com.mouritech.security_frontend.mainserviceImpl;

import com.mouritech.security_frontend.cartdto.CartItemResponse;
import com.mouritech.security_frontend.config.RazorpayConfig;
import com.mouritech.security_frontend.mainentity.*;
import com.mouritech.security_frontend.mainservice.OrderService;
import com.mouritech.security_frontend.orderdto.*;
import com.mouritech.security_frontend.repository.CartItemRepository;
import com.mouritech.security_frontend.repository.OrderRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private RazorpayConfig razorpayConfig;

    @Override
    public OrderResponse placeOrder(OrderRequest request, String userEmail) {
        List<CartItem> cartItems = cartItemRepository.findByUserEmail(userEmail);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(i -> i.getQuantity() * i.getProduct().getProductPrice())
                .sum();

        String razorpayOrderId = createRazorpayOrder(totalAmount);

        // Local order creation
        Order order = new Order();
        order.setUserEmail(userEmail);
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        order.setRazorpayOrderId(razorpayOrderId);

        for (CartItem cartItem : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getQuantity() * cartItem.getProduct().getProductPrice());
            order.getItems().add(item);
        }

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        OrderResponse res = mapToResponse(savedOrder);
        res.setRazorpayOrderId(razorpayOrderId);

        return res;
    }

    private String createRazorpayOrder(double totalAmount) {
        try {
            RazorpayClient client = razorpayConfig.getClient();
            JSONObject options = new JSONObject();
            options.put("amount", (int) (totalAmount * 100)); // in paise
            options.put("currency", "INR");
            options.put("receipt", "txn_" + System.currentTimeMillis());

            com.razorpay.Order razorOrder = client.orders.create(options);
            return razorOrder.get("id").toString();
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage());
        }
    }

    @Override
    public List<OrderResponse> getOrders(String userEmail) {
        return orderRepository.findByUserEmail(userEmail)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setOrderId(order.getOrderId());
        res.setShippingAddress(order.getShippingAddress());
        res.setPaymentMethod(order.getPaymentMethod());
        res.setOrderDate(order.getOrderDate());
        res.setTotalAmount(order.getTotalAmount());
        res.setItems(order.getItems().stream().map(item -> {
            OrderItemResponse r = new OrderItemResponse();
            r.setProductName(item.getProduct().getProductName());
            r.setQuantity(item.getQuantity());
            r.setPrice(item.getPrice());
            return r;
        }).collect(Collectors.toList()));
        res.setRazorpayOrderId(order.getRazorpayOrderId());
        return res;
    }
}
