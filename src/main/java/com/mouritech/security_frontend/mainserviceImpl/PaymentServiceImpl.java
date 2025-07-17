package com.mouritech.security_frontend.mainserviceImpl;

import com.mouritech.security_frontend.mainentity.Order;
import com.mouritech.security_frontend.mainentity.Payment;
import com.mouritech.security_frontend.mainentity.PaymentStatus;
import com.mouritech.security_frontend.mainservice.PaymentService;
import com.mouritech.security_frontend.paymentdto.PaymentRequest;
import com.mouritech.security_frontend.paymentdto.PaymentResponse;
import com.mouritech.security_frontend.repository.OrderRepository;
import com.mouritech.security_frontend.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public PaymentResponse processPayment(PaymentRequest request, String userEmail) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setUserEmail(userEmail);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setRazorpayPaymentId(request.getRazorpayPaymentId()); 

        
        payment.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);

        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getId());
        response.setStatus(payment.getStatus());
        response.setPaymentDate(payment.getPaymentDate());

        return response;
    }

}
