package com.example.swd391_be_hiv.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MedicalRecord_ID")
     Long medicalRecordId;

    @ManyToOne
    @JoinColumn(name = "User_ID")
     Customer customer;

    @ManyToOne
    @JoinColumn(name = "Doctor_ID")
     Doctor doctor;

    @Column(name = "Last_updated")
     LocalDateTime lastUpdated;

    @Column(name = "CD4_count")
     Integer cd4Count;

    @Column(name = "Viral_load")
     Double viralLoad;

    @Column(name = "Treatment_history")
     String treatmentHistory;

    // Relationships
    @OneToMany(mappedBy = "medicalRecord")
     List<LabResult> labResults;

    @OneToMany(mappedBy = "medicalRecord")
     List<TreatmentPlan> treatmentPlans;
}