package com.example.swd391_be_hiv.api;

import com.example.swd391_be_hiv.entity.Appointment;
import com.example.swd391_be_hiv.entity.Customer;
import com.example.swd391_be_hiv.entity.Doctor;
import com.example.swd391_be_hiv.model.request.AppointmentRequest;
import com.example.swd391_be_hiv.repository.AppointmentRepository;
import com.example.swd391_be_hiv.repository.CustomerRepository;
import com.example.swd391_be_hiv.repository.DoctorRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "api")
@RestController
@RequestMapping("api")
public class AppointmentAPI {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(@RequestBody AppointmentRequest request) {
        Optional<Customer> customerOpt = customerRepository.findById(request.getCustomerId());
        Optional<Doctor> doctorOpt = doctorRepository.findById(request.getDoctorId());

        if (customerOpt.isEmpty() || doctorOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid customer or doctor ID");
        }

        Appointment appointment = new Appointment();
        appointment.setCustomer(customerOpt.get());
        appointment.setDoctor(doctorOpt.get());
        appointment.setType(request.getType());
        appointment.setNote(request.getNote());
        appointment.setDatetime(request.getDatetime());
        appointment.setStatus("PENDING");

        appointmentRepository.save(appointment);
        return ResponseEntity.ok("Appointment booked successfully.");
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(appointmentRepository.findByCustomer_Id(customerId));
    }
}
