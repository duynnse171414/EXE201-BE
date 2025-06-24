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
public class AppointmentDetailResponse {

    private Long appointmentId;

    // Customer details
    private CustomerInfo customer;

    // Doctor details
    private DoctorInfo doctor;

    private String type;
    private String note;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime datetime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerInfo {
        private Long id;
        private String fullName;
        private String phone;
        private String email;
        private String gender;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorInfo {
        private Long id;
        private String fullName;
        private String specialization;

    }

    // Constructor để convert từ Entity sang DetailResponse
    public AppointmentDetailResponse(Appointment appointment) {
        this.appointmentId = appointment.getAppointmentId();

        // Customer info
        if (appointment.getCustomer() != null) {
            this.customer = new CustomerInfo(
                    appointment.getCustomer().getId(),
                    appointment.getCustomer().getAccount().getFullName(),
                    appointment.getCustomer().getAccount().getPhone(),
                    appointment.getCustomer().getAccount().getEmail(),
                    appointment.getCustomer().getAccount().getGender()

            );
        }

        // Doctor info
        if (appointment.getDoctor() != null) {
            this.doctor = new DoctorInfo(
                    appointment.getDoctor().getId(),
                    appointment.getDoctor().getName(),
                    appointment.getDoctor().getSpecialization()

            );
        }

        this.type = appointment.getType();
        this.note = appointment.getNote();
        this.status = appointment.getStatus();
        this.datetime = appointment.getDatetime();

    }


    public static AppointmentDetailResponse fromEntity(Appointment appointment) {
        return new AppointmentDetailResponse(appointment);
    }
}