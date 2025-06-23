package com.example.swd391_be_hiv.model.reponse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LabResultResponse {

    private Long labResultId;
    private Long medicalRecordId;
    private String patientName; // From medical record
    private Long doctorId;
    private String doctorName; // From doctor
    private String result;
    private Integer cd4Count;
    private LocalDate testDate;
    private String note;
}