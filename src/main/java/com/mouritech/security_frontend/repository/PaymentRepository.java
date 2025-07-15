package com.mouritech.security_frontend.repository;

import com.mouritech.security_frontend.mainentity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}