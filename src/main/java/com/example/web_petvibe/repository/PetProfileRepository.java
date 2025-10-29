package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.PetProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetProfileRepository extends JpaRepository<PetProfile, Long> {

    // Tìm tất cả pet profiles chưa bị xóa
    @Query("SELECT p FROM PetProfile p WHERE p.isDeleted = false")
    List<PetProfile> findAllActive();

    // Tìm pet profile theo ID (chưa bị xóa)
    @Query("SELECT p FROM PetProfile p WHERE p.petId = :petId AND p.isDeleted = false")
    Optional<PetProfile> findByIdActive(@Param("petId") Long petId);

    // Tìm pet profiles theo userId - SỬA QUERY NÀY
    @Query("SELECT p FROM PetProfile p WHERE p.account.id = :userId AND p.isDeleted = false")
    List<PetProfile> findByUserIdActive(@Param("userId") Long userId);

    // Tìm pet profiles theo pet type (chưa bị xóa)
    @Query("SELECT p FROM PetProfile p WHERE p.petType = :petType AND p.isDeleted = false")
    List<PetProfile> findByPetTypeActive(@Param("petType") String petType);
}