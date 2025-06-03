package com.example.swd391_be_hiv.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

@Entity
@Getter
@Setter

public class LabResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LabResult_ID")
     Long labResultId;

    @ManyToOne
    @JoinColumn(name = "MedicalRecord_ID")
     MedicalRecord medicalRecord;

    @Column(name = "Doctor_ID")
     Long doctorId;

    @Column(name = "Result")
     String result;

    @Column(name = "CD4_count")
     Integer cd4Count;

    @Column(name = "Test_date")
     LocalDate testDate;

    @Column(name = "Note")
     String note;
}