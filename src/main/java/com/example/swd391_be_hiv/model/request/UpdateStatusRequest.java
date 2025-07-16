package com.example.swd391_be_hiv.model.request;

import lombok.Data;

@Data
public class UpdateStatusRequest {
    private String status;
    private String note; // Optional: reason for status change
}