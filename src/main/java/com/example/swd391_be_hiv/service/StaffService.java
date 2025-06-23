package com.example.swd391_be_hiv.service;

import com.example.swd391_be_hiv.entity.Staff;
import com.example.swd391_be_hiv.exception.DuplicateEntity;
import com.example.swd391_be_hiv.exception.NotFoundException;
import com.example.swd391_be_hiv.model.reponse.StaffResponse;
import com.example.swd391_be_hiv.model.request.StaffRequest;
import com.example.swd391_be_hiv.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {

    @Autowired
    StaffRepository staffRepository;

    public StaffResponse createStaff(StaffRequest requestDTO) {
        try {
            // Check if email already exists
            if (staffRepository.findStaffByEmail(requestDTO.getEmail()) != null) {
                throw new DuplicateEntity("Email already exists");
            }

            // Convert DTO to entity
            Staff staff = new Staff();
            staff.setName(requestDTO.getName());
            staff.setEmail(requestDTO.getEmail());
            staff.setPhone(requestDTO.getPhone());
            staff.setGender(requestDTO.getGender());
            staff.setPassword(requestDTO.getPassword());
            staff.setIsDeleted(false);

            Staff savedStaff = staffRepository.save(staff);
            return convertToResponseDTO(savedStaff);

        } catch (Exception e) {
            throw new DuplicateEntity("Error creating staff: " + e.getMessage());
        }
    }

    public List<StaffResponse> getAllStaff() {
        return staffRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StaffResponse> getAllActiveStaff() {
        return staffRepository.findStaffByIsDeletedFalse()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public StaffResponse getStaffById(Long staffId) {
        Staff staff = staffRepository.findStaffByStaffId(staffId);
        if (staff == null) {
            throw new NotFoundException("Staff not found");
        }
        return convertToResponseDTO(staff);
    }

    public StaffResponse updateStaff(StaffRequest requestDTO, Long staffId) {
        Staff existingStaff = staffRepository.findStaffByStaffId(staffId);
        if (existingStaff == null) {
            throw new NotFoundException("Staff not found");
        }

        // Check if email already exists for other staff
        Staff emailExists = staffRepository.findStaffByEmail(requestDTO.getEmail());
        if (emailExists != null && !emailExists.getStaffId().equals(staffId)) {
            throw new DuplicateEntity("Email already exists");
        }

        // Update fields
        existingStaff.setName(requestDTO.getName());
        existingStaff.setEmail(requestDTO.getEmail());
        existingStaff.setPhone(requestDTO.getPhone());
        existingStaff.setGender(requestDTO.getGender());
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            existingStaff.setPassword(requestDTO.getPassword());
        }

        Staff updatedStaff = staffRepository.save(existingStaff);
        return convertToResponseDTO(updatedStaff);
    }

    public StaffResponse deleteStaff(Long staffId) {
        Staff staff = staffRepository.findStaffByStaffId(staffId);
        if (staff == null) {
            throw new NotFoundException("Staff not found");
        }

        // Soft delete
        staff.setIsDeleted(true);
        Staff deletedStaff = staffRepository.save(staff);
        return convertToResponseDTO(deletedStaff);
    }

    public List<StaffResponse> getStaffByGender(String gender) {
        return staffRepository.findStaffByGender(gender)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public StaffResponse getStaffByEmail(String email) {
        Staff staff = staffRepository.findStaffByEmail(email);
        if (staff == null) {
            throw new NotFoundException("Staff not found");
        }
        return convertToResponseDTO(staff);
    }

    public List<StaffResponse> searchStaffByName(String name) {
        return staffRepository.findStaffByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private StaffResponse convertToResponseDTO(Staff staff) {
        StaffResponse dto = new StaffResponse();
        dto.setStaffId(staff.getStaffId());
        dto.setName(staff.getName());
        dto.setEmail(staff.getEmail());
        dto.setPhone(staff.getPhone());
        dto.setGender(staff.getGender());
        dto.setIsDeleted(staff.getIsDeleted());
        // Don't include password in response for security
        return dto;
    }
}