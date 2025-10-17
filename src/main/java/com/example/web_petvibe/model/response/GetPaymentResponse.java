
package com.example.web_petvibe.model.response;

import com.example.web_petvibe.entity.Payment;
import com.example.web_petvibe.entity.Payment.PaymentStatus;
import com.example.web_petvibe.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPaymentResponse {
    private String message;
    private boolean success;
    private Long paymentId;
    private Long orderId;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private String transactionId;
}