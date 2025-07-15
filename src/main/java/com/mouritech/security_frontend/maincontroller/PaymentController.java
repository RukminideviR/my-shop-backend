package com.mouritech.security_frontend.maincontroller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mouritech.security_frontend.mainentity.Order;
import com.mouritech.security_frontend.mainentity.PaymentStatus;
import com.mouritech.security_frontend.repository.OrderRepository;
import com.razorpay.Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private static final String RAZORPAY_SECRET = "xRhyKQKTiePUUVwhebnYITA5";

    @Autowired
    private OrderRepository orderRepository;


@PostMapping("/verify")
public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> paymentData) {
    String razorpayPaymentId = paymentData.get("razorpayPaymentId");
    String razorpayOrderId = paymentData.get("razorpayOrderId");
    String razorpaySignature = paymentData.get("razorpaySignature");

    if (razorpayPaymentId == null || razorpayOrderId == null || razorpaySignature == null) {
        return ResponseEntity.badRequest().body("Missing payment details");
    }

    try {
        // ✅ Create JSONObject instead of Map
        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id", razorpayOrderId);
        attributes.put("razorpay_payment_id", razorpayPaymentId);
        attributes.put("razorpay_signature", razorpaySignature);

        boolean isValid = Utils.verifyPaymentSignature(attributes, RAZORPAY_SECRET);

        if (isValid) {
            Order order = orderRepository.findByRazorpayOrderId(razorpayOrderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            order.setPaymentStatus(PaymentStatus.SUCCESS);
            orderRepository.save(order);

            return ResponseEntity.ok("Payment verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payment signature");
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error verifying payment: " + e.getMessage());
    }
}
}
