package com.mouritech.security_frontend.paymentdto;

import java.time.LocalDateTime;

import com.mouritech.security_frontend.mainentity.PaymentStatus;

public class PaymentResponse {
    private Long paymentId;
    private PaymentStatus status;
    private LocalDateTime paymentDate;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}