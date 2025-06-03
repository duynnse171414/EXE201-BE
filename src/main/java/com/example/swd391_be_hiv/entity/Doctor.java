package com.example.swd391_be_hiv.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Entity
@Getter
@Setter
public class Doctor {

    @Id
    @Column(name = "Doctor_ID")
     Long doctorId;

    @Column(name = "User_ID")
     Long userId;

    @Column(name = "Qualifications")
     String qualifications;

    @Column(name = "Specialization")
     String specialization;

    @Column(name = "Work_schedule")
     String workSchedule;

    // Fixed relationships
    @OneToMany(mappedBy = "doctor")
     List<MedicalRecord> medicalRecords;

    @OneToMany(mappedBy = "doctor")
     List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor")
     List<TreatmentPlan> treatmentPlans;

    @OneToMany
    @JoinColumn(name = "Doctor_ID")
     List<LabResult> labResults;
}