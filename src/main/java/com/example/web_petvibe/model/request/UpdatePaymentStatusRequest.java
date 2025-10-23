package com.example.web_petvibe.model.request;

import lombok.Data;

@Data
public class UpdatePaymentStatusRequest {
    private String paymentStatus; // PENDING, COMPLETED, FAILED, EXPIRED
}