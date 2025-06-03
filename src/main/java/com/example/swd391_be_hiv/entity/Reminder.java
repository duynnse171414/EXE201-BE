package com.example.swd391_be_hiv.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Reminder_ID")
     Long reminderId;

    @ManyToOne
    @JoinColumn(name = "User_ID")
     Customer customer;

    @Column(name = "Reminder_type")
     String reminderType;

    @Column(name = "Message")
     String message;

    @Column(name = "Is_sent")
     Boolean isSent;

    @Column(name = "Reminder_date")
     LocalDate reminderDate;
}
