package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.Payment;
import com.example.web_petvibe.entity.Payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.isDeleted = false")
    List<Payment> findAllActive();

    @Query("SELECT p FROM Payment p WHERE p.paymentId = ?1 AND p.isDeleted = false")
    Optional<Payment> findByIdActive(Long id);

    @Query("SELECT p FROM Payment p WHERE p.orderId = ?1 AND p.isDeleted = false")
    List<Payment> findByOrderId(Long orderId);

    @Query("SELECT p FROM Payment p WHERE p.status = ?1 AND p.isDeleted = false")
    List<Payment> findByStatus(PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.transactionId = ?1 AND p.isDeleted = false")
    Optional<Payment> findByTransactionId(String transactionId);

    @Query("SELECT p FROM Payment p WHERE p.paymentMethod.paymentMethodId = ?1 AND p.isDeleted = false")
    List<Payment> findByPaymentMethodId(Long paymentMethodId);
}