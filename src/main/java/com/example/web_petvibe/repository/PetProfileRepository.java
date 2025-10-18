package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.PetProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetProfileRepository extends JpaRepository<PetProfile, Long> {

    @Query("SELECT p FROM PetProfile p WHERE p.isDeleted = false")
    List<PetProfile> findAllActive();

    @Query("SELECT p FROM PetProfile p WHERE p.petId = ?1 AND p.isDeleted = false")
    Optional<PetProfile> findByIdActive(Long petId);

    @Query("SELECT p FROM PetProfile p WHERE p.userId = ?1 AND p.isDeleted = false")
    List<PetProfile> findByUserIdActive(Long userId);

    @Query("SELECT p FROM PetProfile p WHERE p.petType = ?1 AND p.isDeleted = false")
    List<PetProfile> findByPetTypeActive(String petType);
}