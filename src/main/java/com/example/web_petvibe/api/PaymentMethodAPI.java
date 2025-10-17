package com.example.web_petvibe.api;

import com.example.web_petvibe.entity.PaymentMethod;
import com.example.web_petvibe.model.request.PaymentMethodRequest;
import com.example.web_petvibe.model.response.ApiResponse;
import com.example.web_petvibe.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment-methods")
@SecurityRequirement(name = "api")
public class PaymentMethodAPI {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/getAll")
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods() {
        try {
            return ResponseEntity.ok(paymentMethodService.getAllPaymentMethods());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentMethodById(@PathVariable Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodService.getPaymentMethodById(id);
        if (paymentMethod.isPresent()) {
            return ResponseEntity.ok(paymentMethod.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Payment method not found with id: " + id, false));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPaymentMethod(@Valid @RequestBody PaymentMethodRequest request) {
        try {
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setMethodName(request.getMethodName());
            paymentMethod.setDescription(request.getDescription());
            if (request.getIsActive() != null) {
                paymentMethod.setIsActive(request.getIsActive());
            }

            PaymentMethod created = paymentMethodService.createPaymentMethod(paymentMethod);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Payment method created successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePaymentMethod(
            @PathVariable Long id,
            @Valid @RequestBody PaymentMethodRequest request) {
        try {
            PaymentMethod updatedPaymentMethod = new PaymentMethod();
            updatedPaymentMethod.setMethodName(request.getMethodName());
            updatedPaymentMethod.setDescription(request.getDescription());
            updatedPaymentMethod.setIsActive(request.getIsActive());

            PaymentMethod result = paymentMethodService.updatePaymentMethod(id, updatedPaymentMethod);
            return ResponseEntity.ok(new ApiResponse("Payment method updated successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable Long id) {
        try {
            paymentMethodService.deletePaymentMethod(id);
            return ResponseEntity.ok(new ApiResponse("Payment method deleted successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }
}