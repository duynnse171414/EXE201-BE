package com.example.swd391_be_hiv.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReminderRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Reminder type is required")
    private String reminderType;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Reminder date is required")
    private LocalDate reminderDate;
}
