package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.PaymentMethod;
import com.example.web_petvibe.repository.PaymentMethodRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAllActive();
    }

    public Optional<PaymentMethod> getPaymentMethodById(Long id) {
        return paymentMethodRepository.findByIdActive(id);
    }

    public PaymentMethod createPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod.getMethodName() == null || paymentMethod.getMethodName().trim().isEmpty()) {
            throw new RuntimeException("Payment method name is required");
        }

        // Check if method name already exists
        Optional<PaymentMethod> existing = paymentMethodRepository.findByMethodNameActive(paymentMethod.getMethodName());
        if (existing.isPresent()) {
            throw new RuntimeException("Payment method with name '" + paymentMethod.getMethodName() + "' already exists");
        }

        paymentMethod.setIsActive(true);
        return paymentMethodRepository.save(paymentMethod);
    }

    public PaymentMethod updatePaymentMethod(Long id, PaymentMethod updatedPaymentMethod) {
        Optional<PaymentMethod> paymentMethodOpt = paymentMethodRepository.findByIdActive(id);

        if (!paymentMethodOpt.isPresent()) {
            throw new RuntimeException("Payment method not found with id: " + id);
        }

        PaymentMethod paymentMethod = paymentMethodOpt.get();

        if (updatedPaymentMethod.getMethodName() != null) {
            paymentMethod.setMethodName(updatedPaymentMethod.getMethodName());
        }
        if (updatedPaymentMethod.getDescription() != null) {
            paymentMethod.setDescription(updatedPaymentMethod.getDescription());
        }
        if (updatedPaymentMethod.getIsActive() != null) {
            paymentMethod.setIsActive(updatedPaymentMethod.getIsActive());
        }

        return paymentMethodRepository.save(paymentMethod);
    }

    public void deletePaymentMethod(Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findByIdActive(id);
        if (paymentMethod.isPresent()) {
            PaymentMethod pm = paymentMethod.get();
            pm.setIsActive(false);
            paymentMethodRepository.save(pm);
        } else {
            throw new RuntimeException("Payment method not found with id: " + id);
        }
    }
}