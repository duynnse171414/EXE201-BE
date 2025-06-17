package com.example.swd391_be_hiv.model.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MedicalRecordRequest {

    @NotNull(message = "Customer ID cannot be null")
    @Positive(message = "Customer ID must be positive")
    private Long customerId;

    @NotNull(message = "Doctor ID cannot be null")
    @Positive(message = "Doctor ID must be positive")
    private Long doctorId;

    @Min(value = 0, message = "CD4 count must be non-negative")
    @Max(value = 2000, message = "CD4 count must not exceed 2000")
    private Integer cd4Count;

    @DecimalMin(value = "0.0", message = "Viral load must be non-negative")
    @DecimalMax(value = "10000000.0", message = "Viral load must not exceed 10,000,000")
    private Double viralLoad;

    @Size(max = 2000, message = "Treatment history must not exceed 2000 characters")
    private String treatmentHistory;
}