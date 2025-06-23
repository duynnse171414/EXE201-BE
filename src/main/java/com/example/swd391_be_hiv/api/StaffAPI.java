package com.example.swd391_be_hiv.api;

import com.example.swd391_be_hiv.model.reponse.StaffResponse;
import com.example.swd391_be_hiv.model.request.StaffRequest;
import com.example.swd391_be_hiv.service.StaffService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "api")
@RestController
@RequestMapping("/api/staff")
public class StaffAPI {

    @Autowired
    StaffService staffService;

    @PostMapping
    public ResponseEntity<StaffResponse> createStaff(@Valid @RequestBody StaffRequest requestDTO) {
        StaffResponse response = staffService.createStaff(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<StaffResponse>> getAllStaff() {
        List<StaffResponse> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/active")
    public ResponseEntity<List<StaffResponse>> getAllActiveStaff() {
        List<StaffResponse> staffList = staffService.getAllActiveStaff();
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffResponse> getStaffById(@PathVariable Long id) {
        StaffResponse response = staffService.getStaffById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffResponse> updateStaff(@Valid @RequestBody StaffRequest requestDTO, @PathVariable Long id) {
        StaffResponse response = staffService.updateStaff(requestDTO, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StaffResponse> deleteStaff(@PathVariable Long id) {
        StaffResponse response = staffService.deleteStaff(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<StaffResponse>> getStaffByGender(@PathVariable String gender) {
        List<StaffResponse> staffList = staffService.getStaffByGender(gender);
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StaffResponse> getStaffByEmail(@PathVariable String email) {
        StaffResponse response = staffService.getStaffByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StaffResponse>> searchStaffByName(@RequestParam String name) {
        List<StaffResponse> staffList = staffService.searchStaffByName(name);
        return ResponseEntity.ok(staffList);
    }
}