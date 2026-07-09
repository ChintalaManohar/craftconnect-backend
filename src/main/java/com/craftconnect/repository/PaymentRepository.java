package com.craftconnect.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.craftconnect.entity.Payment;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment>
    findByRazorpayOrderId(
            String razorpayOrderId);
    
    Optional<Payment> findByOrderId(
            Long orderId);
    
    
    @Query("""
    		SELECT COALESCE(SUM(p.amount),0)
    		FROM Payment p
    		WHERE p.status='SUCCESS'
    		""")
    		BigDecimal getTotalRevenue();
}