package com.mouritech.security_frontend.maincontroller;

import com.mouritech.security_frontend.mainservice.OrderService;
import com.mouritech.security_frontend.mainservice.PaymentService;
import com.mouritech.security_frontend.orderdto.OrderRequest;
import com.mouritech.security_frontend.orderdto.OrderResponse;
import com.mouritech.security_frontend.paymentdto.PaymentRequest;
import com.mouritech.security_frontend.paymentdto.PaymentResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasAuthority('USER')")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;  // Add this

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request,
                                                    @AuthenticationPrincipal UserDetails user) {
        // 1️⃣ Place the order
        OrderResponse orderResponse = orderService.placeOrder(request, user.getUsername());

        // 2️⃣ Create a payment entry
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(orderResponse.getOrderId());
        paymentRequest.setPaymentMethod(request.getPaymentMethod());

        PaymentResponse paymentResponse = paymentService.processPayment(paymentRequest, user.getUsername());

        // 3️⃣ Attach payment info to response
        orderResponse.setPaymentId(paymentResponse.getPaymentId());
        orderResponse.setPaymentStatus(paymentResponse.getStatus());

        return ResponseEntity.ok(orderResponse);
    }


    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(orderService.getOrders(user.getUsername()));
    }
}
