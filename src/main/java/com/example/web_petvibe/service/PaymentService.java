package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.Payment;
import com.example.web_petvibe.entity.Payment.PaymentStatus;
import com.example.web_petvibe.entity.PaymentMethod;
import com.example.web_petvibe.repository.PaymentMethodRepository;
import com.example.web_petvibe.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAllActive();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findByIdActive(id);
    }

    public List<Payment> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    public Payment createPayment(Payment payment, Long paymentMethodId) {
        if (payment.getOrderId() == null) {
            throw new RuntimeException("Order ID is required");
        }

        if (payment.getAmount() == null || payment.getAmount().signum() <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        Optional<PaymentMethod> paymentMethodOpt = paymentMethodRepository.findByIdActive(paymentMethodId);
        if (!paymentMethodOpt.isPresent()) {
            throw new RuntimeException("Payment method not found with id: " + paymentMethodId);
        }

        PaymentMethod paymentMethod = paymentMethodOpt.get();
        if (!paymentMethod.getIsActive()) {
            throw new RuntimeException("Payment method is not active");
        }

        payment.setPaymentMethod(paymentMethod);
        if (payment.getStatus() == null) {
            payment.setStatus(PaymentStatus.pending);
        }
        payment.setDeleted(false);

        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Long id, Payment updatedPayment, Long paymentMethodId) {
        Optional<Payment> paymentOpt = paymentRepository.findByIdActive(id);

        if (!paymentOpt.isPresent()) {
            throw new RuntimeException("Payment not found with id: " + id);
        }

        Payment payment = paymentOpt.get();

        if (paymentMethodId != null) {
            Optional<PaymentMethod> paymentMethodOpt = paymentMethodRepository.findByIdActive(paymentMethodId);
            if (!paymentMethodOpt.isPresent()) {
                throw new RuntimeException("Payment method not found with id: " + paymentMethodId);
            }
            payment.setPaymentMethod(paymentMethodOpt.get());
        }

        if (updatedPayment.getAmount() != null) {
            payment.setAmount(updatedPayment.getAmount());
        }
        if (updatedPayment.getStatus() != null) {
            payment.setStatus(updatedPayment.getStatus());
        }
        if (updatedPayment.getTransactionId() != null) {
            payment.setTransactionId(updatedPayment.getTransactionId());
        }

        return paymentRepository.save(payment);
    }

    public Payment updatePaymentStatus(Long id, PaymentStatus status) {
        Optional<Payment> paymentOpt = paymentRepository.findByIdActive(id);

        if (!paymentOpt.isPresent()) {
            throw new RuntimeException("Payment not found with id: " + id);
        }

        Payment payment = paymentOpt.get();
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {
        Optional<Payment> payment = paymentRepository.findByIdActive(id);
        if (payment.isPresent()) {
            Payment p = payment.get();
            p.setDeleted(true);
            paymentRepository.save(p);
        } else {
            throw new RuntimeException("Payment not found with id: " + id);
        }
    }
}