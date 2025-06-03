package com.example.swd391_be_hiv.model.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    private Long customerId;
    private Long doctorId;
    private String type;
    private String note;
    private LocalDateTime datetime;
}
