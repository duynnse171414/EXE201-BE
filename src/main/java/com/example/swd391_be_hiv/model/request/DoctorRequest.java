package com.example.swd391_be_hiv.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DoctorRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    String name;

    @JsonIgnore
    private boolean deleted = false;

    @NotBlank(message = "Qualifications is required")
    private String qualifications;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    private String workSchedule;





}
