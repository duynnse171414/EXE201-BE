package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.isActive = true")
    List<PaymentMethod> findAllActive();

    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.paymentMethodId = ?1 AND pm.isActive = true")
    Optional<PaymentMethod> findByIdActive(Long id);

    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.methodName = ?1 AND pm.isActive = true")
    Optional<PaymentMethod> findByMethodNameActive(String methodName);
}