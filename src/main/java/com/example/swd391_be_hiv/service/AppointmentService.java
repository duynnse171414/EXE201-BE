package com.example.swd391_be_hiv.service;

import com.example.swd391_be_hiv.entity.Appointment;
import com.example.swd391_be_hiv.entity.Customer;
import com.example.swd391_be_hiv.entity.Doctor;
import com.example.swd391_be_hiv.model.request.AppointmentRequest;
import com.example.swd391_be_hiv.repository.AppointmentRepository;
import com.example.swd391_be_hiv.repository.CustomerRepository;
import com.example.swd391_be_hiv.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public String bookAppointment(AppointmentRequest request) {
        Optional<Customer> customerOpt = customerRepository.findById(request.getCustomerId());
        Optional<Doctor> doctorOpt = doctorRepository.findById(request.getDoctorId());

        if (customerOpt.isEmpty() || doctorOpt.isEmpty()) {
            return "Invalid customer or doctor ID";
        }

        Appointment appointment = new Appointment();
        appointment.setCustomer(customerOpt.get());
        appointment.setDoctor(doctorOpt.get());
        appointment.setType(request.getType());
        appointment.setNote(request.getNote());
        appointment.setDatetime(request.getDatetime());
        appointment.setStatus("PENDING");

        appointmentRepository.save(appointment);
        return "Appointment booked successfully.";
    }

    public List<Appointment> getAppointmentsByCustomer(Long customerId) {
        return appointmentRepository.findByCustomer_Id(customerId);
    }

    public List<Appointment> getAllAppointment() {
        List<Appointment> appointments = appointmentRepository.findByDeletedFalse();
        return appointments;
    }


    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID không được để trống");
        }
        return appointmentRepository.findByDoctorIdAndNotDeleted(doctorId);
    }

    /**
     * Lấy appointments theo Doctor ID và Status
     */
    public List<Appointment> getAppointmentsByDoctorIdAndStatus(Long doctorId, String status) {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID không được để trống");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status không được để trống");
        }
        return appointmentRepository.findByDoctorIdAndStatusAndNotDeleted(doctorId, status);
    }

    /**
     * Kiểm tra xem doctor có appointments hay không
     */
    public boolean hasDoctorAppointments(Long doctorId) {
        List<Appointment> appointments = getAppointmentsByDoctorId(doctorId);
        return !appointments.isEmpty();
    }

    /**
     * Đếm số lượng appointments của doctor
     */
    public long countAppointmentsByDoctorId(Long doctorId) {
        List<Appointment> appointments = getAppointmentsByDoctorId(doctorId);
        return appointments.size();
    }
}

