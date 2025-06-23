package com.example.swd391_be_hiv.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LabResultRequest {

    @NotNull(message = "Medical Record ID is required")
    private Long medicalRecordId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotBlank(message = "Result is required")
    private String result;

    @Min(value = 0, message = "CD4 count must be non-negative")
    private Integer cd4Count;

    @NotNull(message = "Test date is required")
    private LocalDate testDate;

    private String note; // Optional field
}