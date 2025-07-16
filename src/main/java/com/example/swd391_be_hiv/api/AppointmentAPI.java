package com.example.swd391_be_hiv.api;

import com.example.swd391_be_hiv.model.request.AppointmentRequest;
import com.example.swd391_be_hiv.model.request.UpdateStatusRequest;
import com.example.swd391_be_hiv.model.reponse.AppointmentResponse;
import com.example.swd391_be_hiv.model.reponse.AppointmentDetailResponse;
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
    private AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> bookAppointment(@RequestBody AppointmentRequest request) {
        try {
            String result = appointmentService.bookAppointment(request);

            Map<String, Object> response = new HashMap<>();
            if (result.contains("successfully")) {
                response.put("success", true);
                response.put("message", result);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", result);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Có lỗi xảy ra khi đặt appointment");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Map<String, Object>> getAppointmentsByCustomer(@PathVariable Long customerId) {
        try {
            List<AppointmentResponse> appointments = appointmentService.getAppointmentsByCustomer(customerId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Lấy danh sách appointments thành công");
            response.put("data", appointments);
            response.put("total", appointments.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Có lỗi xảy ra khi lấy danh sách appointments");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("getAllAppointment")
    public ResponseEntity<Map<String, Object>> getAllAppointments() {
        try {
            List<AppointmentResponse> appointmentList = appointmentService.getAllAppointment();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Lấy tất cả appointments thành công");
            response.put("data", appointmentList);
            response.put("total", appointmentList.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Có lỗi xảy ra khi lấy danh sách appointments");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Map<String, Object>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        try {
            List<AppointmentResponse> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);

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

    @GetMapping("/doctor/{doctorId}/status/{status}")
    public ResponseEntity<Map<String, Object>> getAppointmentsByDoctorIdAndStatus(
            @PathVariable Long doctorId,
            @PathVariable String status) {
        try {
            List<AppointmentResponse> appointments = appointmentService.getAppointmentsByDoctorIdAndStatus(doctorId, status);

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

    @GetMapping("/doctor/{doctorId}/count")
    public ResponseEntity<Map<String, Object>> countAppointmentsByDoctorId(@PathVariable Long doctorId) {
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


    @GetMapping("/{appointmentId}")
    public ResponseEntity<Map<String, Object>> getAppointmentDetail(@PathVariable Long appointmentId) {
        try {
            AppointmentDetailResponse appointment = appointmentService.getAppointmentDetail(appointmentId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Lấy chi tiết appointment thành công");
            response.put("data", appointment);

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
            errorResponse.put("message", "Có lỗi xảy ra khi lấy chi tiết appointment");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getAppointmentsByStatus(@PathVariable String status) {
        try {
            List<AppointmentResponse> appointments = appointmentService.getAppointmentsByStatus(status);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Lấy danh sách appointments theo status thành công");
            response.put("data", appointments);
            response.put("total", appointments.size());
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


    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<Map<String, Object>> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestBody UpdateStatusRequest request) {
        try {
            String result = appointmentService.updateAppointmentStatus(appointmentId, request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", result);
            response.put("appointmentId", appointmentId);
            response.put("newStatus", request.getStatus());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("appointmentId", appointmentId);

            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Có lỗi xảy ra khi cập nhật trạng thái appointment");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("appointmentId", appointmentId);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}