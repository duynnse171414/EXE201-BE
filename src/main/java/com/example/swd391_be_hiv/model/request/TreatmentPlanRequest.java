package com.example.swd391_be_hiv.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TreatmentPlanRequest {

    @NotNull(message = "Medical Record ID is required")
    private Long medicalRecordId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotBlank(message = "ARV regimen is required")
    private String arvRegimen;

    @NotBlank(message = "Applicable group is required")
    private String applicableGroup;

    private String note; // Optional field

    @NotNull(message = "Start date is required")
    private LocalDate startDate;
}