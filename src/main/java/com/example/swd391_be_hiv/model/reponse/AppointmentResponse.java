package com.example.swd391_be_hiv.model.reponse;

import com.example.swd391_be_hiv.entity.Appointment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private Long appointmentId;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialty;
    private String type;
    private String note;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime datetime;

    // Constructor để convert từ Entity sang Response
    public AppointmentResponse(Appointment appointment) {
        this.appointmentId = appointment.getAppointmentId();

        // Customer info
        if (appointment.getCustomer() != null) {
            this.customerId = appointment.getCustomer().getId();
            this.customerName = appointment.getCustomer().getAccount().getFullName();
            this.customerPhone = appointment.getCustomer().getAccount().getPhone();
            this.customerEmail = appointment.getCustomer().getAccount().getEmail();
        }

        // Doctor info
        if (appointment.getDoctor() != null) {
            this.doctorId = appointment.getDoctor().getId();
            this.doctorName = appointment.getDoctor().getName();
            this.doctorSpecialty = appointment.getDoctor().getSpecialization();
        }

        this.type = appointment.getType();
        this.note = appointment.getNote();
        this.status = appointment.getStatus();
        this.datetime = appointment.getDatetime();
    }

    // Static method để convert từ Entity sang Response
    public static AppointmentResponse fromEntity(Appointment appointment) {
        return new AppointmentResponse(appointment);
    }
}