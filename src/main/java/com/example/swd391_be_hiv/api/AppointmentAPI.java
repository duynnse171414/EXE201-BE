package com.example.swd391_be_hiv.api;

import com.example.swd391_be_hiv.entity.Appointment;
import com.example.swd391_be_hiv.entity.Customer;
import com.example.swd391_be_hiv.entity.Doctor;
import com.example.swd391_be_hiv.model.request.AppointmentRequest;
import com.example.swd391_be_hiv.repository.AppointmentRepository;
import com.example.swd391_be_hiv.repository.CustomerRepository;
import com.example.swd391_be_hiv.repository.DoctorRepository;
import com.example.swd391_be_hiv.service.AppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SecurityRequirement(name = "api")
@RestController
@RequestMapping("api/appointment")
public class AppointmentAPI {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentService appointmentService;

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

    @GetMapping("getAllAppointment")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointmentList = appointmentService.getAllAppointment();
        return ResponseEntity.ok(appointmentList);
    }



    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Lấy danh sách appointments thành công");
            response.put("data", appointments);
            response.put("total", appointments.size());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("data", null);

            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Có lỗi xảy ra khi lấy danh sách appointments");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET /api/appointments/doctor/{doctorId}/status/{status}
     * Lấy appointments theo Doctor ID và Status
     */
    @GetMapping("/doctor/{doctorId}/status/{status}")
    public ResponseEntity<?> getAppointmentsByDoctorIdAndStatus(
            @PathVariable Long doctorId,
            @PathVariable String status) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByDoctorIdAndStatus(doctorId, status);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Lấy danh sách appointments theo status thành công");
            response.put("data", appointments);
            response.put("total", appointments.size());
            response.put("doctorId", doctorId);
            response.put("status", status);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("data", null);

            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Có lỗi xảy ra khi lấy danh sách appointments");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET /api/appointments/doctor/{doctorId}/count
     * Đếm số lượng appointments của doctor
     */
    @GetMapping("/doctor/{doctorId}/count")
    public ResponseEntity<?> countAppointmentsByDoctorId(@PathVariable Long doctorId) {
        try {
            long count = appointmentService.countAppointmentsByDoctorId(doctorId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đếm appointments thành công");
            response.put("doctorId", doctorId);
            response.put("count", count);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Có lỗi xảy ra khi đếm appointments");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
