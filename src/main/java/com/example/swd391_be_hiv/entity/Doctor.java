package com.example.swd391_be_hiv.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Thêm GeneratedValue
    private Long id;

    String name;

    @Column(name = "customer_id") // Đặt tên column rõ ràng
    private Long customerId;

    @JsonIgnore
    @Column(name = "is_deleted") // Sửa tên field cho đúng với repository
    private boolean deleted = false;

    @Column(name = "qualifications") // Loại bỏ viết hoa không cần thiết
    private String qualifications;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "work_schedule")
    private String workSchedule;

    // Fixed relationships
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Tránh circular reference
    private List<MedicalRecord> medicalRecords;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TreatmentPlan> treatmentPlans;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id") // Sử dụng snake_case
    @JsonIgnore
    private List<LabResult> labResults;

}