package com.example.web_petvibe.api;

import com.example.web_petvibe.entity.Payment;
import com.example.web_petvibe.entity.Payment.PaymentStatus;
import com.example.web_petvibe.model.request.PaymentRequest;
import com.example.web_petvibe.model.response.ApiResponse;
import com.example.web_petvibe.model.response.GetPaymentResponse;
import com.example.web_petvibe.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@SecurityRequirement(name = "api")
public class PaymentAPI {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Payment>> getAllPayments() {
        try {
            return ResponseEntity.ok(paymentService.getAllPayments());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        if (payment.isPresent()) {
            Payment p = payment.get();
            GetPaymentResponse response = new GetPaymentResponse(
                    "Payment found successfully",
                    true,
                    p.getPaymentId(),
                    p.getOrderId(),
                    p.getPaymentMethod(),
                    p.getAmount(),
                    p.getStatus(),
                    p.getPaymentDate(),
                    p.getTransactionId()
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Payment not found with id: " + id, false));
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getPaymentsByOrderId(@PathVariable Long orderId) {
        try {
            List<Payment> payments = paymentService.getPaymentsByOrderId(orderId);
            if (payments.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse("No payments found for order id: " + orderId, true));
            }
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error retrieving payments", false));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        try {
            List<Payment> payments = paymentService.getPaymentsByStatus(status);
            if (payments.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse("No payments found with status: " + status, true));
            }
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error retrieving payments", false));
        }
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<?> getPaymentByTransactionId(@PathVariable String transactionId) {
        Optional<Payment> payment = paymentService.getPaymentByTransactionId(transactionId);
        if (payment.isPresent()) {
            return ResponseEntity.ok(payment.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Payment not found with transaction id: " + transactionId, false));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody PaymentRequest request) {
        try {
            Payment payment = new Payment();
            payment.setOrderId(request.getOrderId());
            payment.setAmount(request.getAmount());
            payment.setStatus(request.getStatus());
            payment.setTransactionId(request.getTransactionId());

            Payment created = paymentService.createPayment(payment, request.getPaymentMethodId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Payment created successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request) {
        try {
            Payment updatedPayment = new Payment();
            updatedPayment.setAmount(request.getAmount());
            updatedPayment.setStatus(request.getStatus());
            updatedPayment.setTransactionId(request.getTransactionId());

            Payment result = paymentService.updatePayment(id, updatedPayment, request.getPaymentMethodId());
            return ResponseEntity.ok(new ApiResponse("Payment updated successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status) {
        try {
            Payment result = paymentService.updatePaymentStatus(id, status);
            return ResponseEntity.ok(new ApiResponse("Payment status updated successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
        try {
            paymentService.deletePayment(id);
            return ResponseEntity.ok(new ApiResponse("Payment deleted successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }
}