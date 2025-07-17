package com.mouritech.security_frontend.mainserviceImpl;

import com.mouritech.security_frontend.config.RazorpayConfig;
import com.mouritech.security_frontend.mainentity.*;
import com.mouritech.security_frontend.mainservice.OrderService;
import com.mouritech.security_frontend.orderdto.*;
import com.mouritech.security_frontend.repository.CartItemRepository;
import com.mouritech.security_frontend.repository.OrderRepository;
import com.mouritech.security_frontend.repository.ProductRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RazorpayConfig razorpayConfig;

    @Override
    public OrderResponse placeOrder(OrderRequest request, String userEmail) {
        List<CartItem> cartItems = cartItemRepository.findByUserEmail(userEmail);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalAmount = 0.0;
        Order order = new Order();
        order.setUserEmail(userEmail);
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setOrderDate(LocalDateTime.now());

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getProductName());
            }

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getQuantity() * product.getProductPrice());
            item.setStatus("PLACED");
            item.setStatusUpdatedDate(LocalDateTime.now());
            order.getItems().add(item);

            totalAmount += item.getPrice();
        }

        order.setTotalAmount(totalAmount);
        order.setRazorpayOrderId(createRazorpayOrder(totalAmount));

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        return mapToResponse(savedOrder);
    }

    private String createRazorpayOrder(double totalAmount) {
        try {
            RazorpayClient client = razorpayConfig.getClient();
            JSONObject options = new JSONObject();
            options.put("amount", (int) (totalAmount * 100));
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
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setOrderId(order.getOrderId());
        res.setShippingAddress(order.getShippingAddress());
        res.setPaymentMethod(order.getPaymentMethod());
        res.setOrderDate(order.getOrderDate());
        res.setTotalAmount(order.getTotalAmount());
        res.setRazorpayOrderId(order.getRazorpayOrderId());
        res.setStatus(order.getStatus());
        res.setDeliveryDate(order.getDeliveryDate());

        res.setItems(order.getItems().stream().map(item -> {
            OrderItemResponse r = new OrderItemResponse();
            r.setItemId(item.getId());
            r.setProductName(item.getProduct().getProductName());
            r.setQuantity(item.getQuantity());
            r.setPrice(item.getPrice());
            r.setStatus(item.getStatus());
            r.setStatusUpdatedDate(item.getStatusUpdatedDate());
            return r;
        }).collect(Collectors.toList()));

        return res;
    }

    private boolean isWithin7Days(LocalDateTime date) {
        return date != null && ChronoUnit.DAYS.between(date, LocalDateTime.now()) <= 7;
    }

    @Override
    public void cancelOrderItem(Long orderId, Long itemId, int quantity, String userEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        OrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        LocalDateTime actionDate = item.getStatusUpdatedDate() != null ? item.getStatusUpdatedDate() : order.getOrderDate();
        if (!isWithin7Days(actionDate)) {
            throw new RuntimeException("Cancel period expired (more than 7 days)");
        }

        if (item.getQuantity() < quantity || quantity < 1) {
            throw new RuntimeException("Invalid quantity to cancel");
        }

        Product product = productRepository.findById(item.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);

        item.setQuantity(item.getQuantity() - quantity);
        if (item.getQuantity() == 0) item.setStatus("CANCELLED");
        item.setStatusUpdatedDate(LocalDateTime.now());

        recalculateTotal(order);
        orderRepository.save(order);
    }

    @Override
    public void returnOrderItem(Long orderId, Long itemId, int quantity, String userEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        OrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        LocalDateTime actionDate = item.getStatusUpdatedDate() != null ? item.getStatusUpdatedDate() : order.getOrderDate();
        if (!isWithin7Days(actionDate)) {
            throw new RuntimeException("Return period expired (more than 7 days)");
        }

        if (item.getQuantity() < quantity || quantity < 1) {
            throw new RuntimeException("Invalid quantity to return");
        }

        Product product = productRepository.findById(item.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);

        item.setQuantity(item.getQuantity() - quantity);
        if (item.getQuantity() == 0) item.setStatus("RETURNED");
        item.setStatusUpdatedDate(LocalDateTime.now());

        recalculateTotal(order);
        orderRepository.save(order);
    }

    private void recalculateTotal(Order order) {
        double newTotal = order.getItems().stream()
            .filter(i -> !"CANCELLED".equals(i.getStatus()) && !"RETURNED".equals(i.getStatus()))
            .mapToDouble(OrderItem::getPrice)
            .sum();
        order.setTotalAmount(newTotal);
    }
}
