package com.example.swd391_be_hiv.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

@Entity
@Getter
@Setter
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Plan_ID")
     Long planId;

    @ManyToOne
    @JoinColumn(name = "MedicalRecord_ID")
     MedicalRecord medicalRecord;

    @ManyToOne
    @JoinColumn(name = "Doctor_ID")
     Doctor doctor;

    @Column(name = "Arv_regimen")
     String arvRegimen;

    @Column(name = "Applicable_group")
     String applicableGroup;

    @Column(name = "Note")
     String note;

    @Column(name = "Start_date")
     LocalDate startDate;
}
