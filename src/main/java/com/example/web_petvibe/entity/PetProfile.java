package com.example.web_petvibe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "pet_profile")
public class PetProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "pet_name", nullable = false)
    private String petName;

    @Column(name = "pet_type")
    private String petType; // dog, cat, bird, etc

    @Column(name = "breed")
    private String breed;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "health_notes", columnDefinition = "TEXT")
    private String healthNotes;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (isDeleted == false) {
            isDeleted = false;
        }
    }
}