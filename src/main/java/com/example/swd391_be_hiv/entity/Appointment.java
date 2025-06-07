package com.example.swd391_be_hiv.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Appointment_ID")
    Long appointmentId;

    @ManyToOne
    @JoinColumn(name = "User_ID")
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "Doctor_ID")
     Doctor doctor;

    @Column(name = "Type")
     String type;

    @Column(name = "Note")
     String note;

    @Column(name = "Status")
     String status;

    @Column(name = "Datetime")
     LocalDateTime datetime;

    @JsonIgnore
    @Column(name = "is_deleted") // Sửa tên field cho đúng với repository
    private boolean deleted = false;
}
