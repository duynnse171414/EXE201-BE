package com.example.web_petvibe.model.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PetProfileDetailResponse {
    private Long petId;
    private Long userId;
    private String fullName;  // Tên của chủ pet
    private String petName;
    private String petType;
    private String breed;
    private LocalDate birthDate;
    private Float weight;
    private String petAge;
    private String petSize;
    private String healthNotes;
    private String imageUrl;
    private LocalDateTime createdAt;
}