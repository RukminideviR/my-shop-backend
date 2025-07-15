package com.mouritech.security_frontend.mainservice;

import com.mouritech.security_frontend.paymentdto.PaymentRequest;
import com.mouritech.security_frontend.paymentdto.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request, String userEmail);
}