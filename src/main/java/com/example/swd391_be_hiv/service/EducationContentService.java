package com.example.swd391_be_hiv.service;

import com.example.swd391_be_hiv.entity.EducationContent;
import com.example.swd391_be_hiv.entity.Staff;
import com.example.swd391_be_hiv.exception.DuplicateEntity;
import com.example.swd391_be_hiv.exception.NotFoundException;
import com.example.swd391_be_hiv.model.reponse.EducationContentResponse;
import com.example.swd391_be_hiv.model.request.EducationContentRequest;
import com.example.swd391_be_hiv.repository.EducationContentRepository;
import com.example.swd391_be_hiv.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EducationContentService {

    @Autowired
    EducationContentRepository educationContentRepository;

    @Autowired
    StaffRepository staffRepository;

    public EducationContentResponse createEducationContent(EducationContentRequest requestDTO) {
        try {
            // Validate staff exists
            Staff staff = staffRepository.findById(requestDTO.getStaffId())
                    .orElseThrow(() -> new NotFoundException("Staff not found"));

            // Convert DTO to entity
            EducationContent educationContent = new EducationContent();
            educationContent.setTitle(requestDTO.getTitle());
            educationContent.setContent(requestDTO.getContent());
            educationContent.setStaff(staff);
            educationContent.setCreatedAt(LocalDateTime.now());

            EducationContent savedContent = educationContentRepository.save(educationContent);
            return convertToResponseDTO(savedContent);

        } catch (Exception e) {
            throw new DuplicateEntity("Error creating education content: " + e.getMessage());
        }
    }

    public List<EducationContentResponse> getAllEducationContents() {
        return educationContentRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public EducationContentResponse updateEducationContent(EducationContentRequest requestDTO, Long postId) {
        EducationContent existingContent = educationContentRepository.findEducationContentByPostId(postId);
        if (existingContent == null) {
            throw new NotFoundException("Education content not found");
        }

        // Validate staff if it's being changed
        Staff staff = staffRepository.findById(requestDTO.getStaffId())
                .orElseThrow(() -> new NotFoundException("Staff not found"));

        // Update fields
        existingContent.setTitle(requestDTO.getTitle());
        existingContent.setContent(requestDTO.getContent());
        existingContent.setStaff(staff);
        // Note: createdAt is not updated to preserve original creation time

        EducationContent updatedContent = educationContentRepository.save(existingContent);
        return convertToResponseDTO(updatedContent);
    }

    public EducationContentResponse deleteEducationContent(Long postId) {
        EducationContent educationContent = educationContentRepository.findEducationContentByPostId(postId);
        if (educationContent == null) {
            throw new NotFoundException("Education content not found");
        }

        EducationContentResponse response = convertToResponseDTO(educationContent);
        educationContentRepository.delete(educationContent);
        return response;
    }

    public List<EducationContentResponse> getEducationContentsByStaff(Long staffId) {
        return educationContentRepository.findEducationContentsByStaff_StaffId(staffId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EducationContentResponse> getEducationContentsByTitleContaining(String keyword) {
        return educationContentRepository.findEducationContentsByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EducationContentResponse> getEducationContentsByContentContaining(String keyword) {
        return educationContentRepository.findEducationContentsByContentContaining(keyword)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EducationContentResponse> getEducationContentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return educationContentRepository.findEducationContentsByCreatedAtBetween(startDate, endDate)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public EducationContentResponse getEducationContentById(Long postId) {
        EducationContent educationContent = educationContentRepository.findEducationContentByPostId(postId);
        if (educationContent == null) {
            throw new NotFoundException("Education content not found");
        }
        return convertToResponseDTO(educationContent);
    }

    private EducationContentResponse convertToResponseDTO(EducationContent educationContent) {
        EducationContentResponse dto = new EducationContentResponse();
        dto.setPostId(educationContent.getPostId());
        dto.setTitle(educationContent.getTitle());
        dto.setContent(educationContent.getContent());
        dto.setStaffId(educationContent.getStaff().getStaffId());
        dto.setStaffName(educationContent.getStaff().getName());
        dto.setCreatedAt(educationContent.getCreatedAt());

        return dto;
    }
}