package com.mouritech.security_frontend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Component
public class RazorpayConfig {
    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    public RazorpayClient getClient() throws RazorpayException {
        return new RazorpayClient(keyId, keySecret);
    }
}

