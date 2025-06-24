package com.example.swd391_be_hiv.api;

import com.example.swd391_be_hiv.entity.Doctor;
import com.example.swd391_be_hiv.model.request.DoctorRequest;
import com.example.swd391_be_hiv.service.DoctorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "api")
@RestController
@RequestMapping("/api/doctor") 
public class DoctorAPI {

    @Autowired
    private DoctorService doctorService;

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody DoctorRequest doctor) {
        Doctor newDoctor = doctorService.createNewDoctor(doctor);
        return ResponseEntity.ok(newDoctor);
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctorList = doctorService.getAllDoctor();
        return ResponseEntity.ok(doctorList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorId(id);
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@Valid @RequestBody Doctor doctor, @PathVariable Long id) {
        Doctor updatedDoctor = doctorService.updateDoctor(doctor, id);
        return ResponseEntity.ok(updatedDoctor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Doctor> deleteDoctor(@PathVariable Long id) {
        Doctor doctor = doctorService.deleteDoctor(id);
        return ResponseEntity.ok(doctor);
    }
}
