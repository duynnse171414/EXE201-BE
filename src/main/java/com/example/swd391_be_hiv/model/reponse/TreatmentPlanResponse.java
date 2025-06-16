package com.example.swd391_be_hiv.model.reponse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TreatmentPlanResponse {

    private Long planId;
    private Long medicalRecordId;
    private String patientName; // From medical record
    private Long doctorId;
    private String doctorName; // From doctor
    private String arvRegimen;
    private String applicableGroup;
    private String note;
    private LocalDate startDate;
}