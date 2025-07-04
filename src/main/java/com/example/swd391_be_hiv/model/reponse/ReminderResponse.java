package com.example.swd391_be_hiv.model.reponse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReminderResponse {

    private Long reminderId;
    private Long customerId;
    private String customerName;
    private String reminderType;
    private String message;
    private Boolean isSent;
    private LocalDate reminderDate;
}