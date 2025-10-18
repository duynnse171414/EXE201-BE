package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.PetProfile;
import com.example.web_petvibe.entity.Account;
import com.example.web_petvibe.repository.PetProfileRepository;
import com.example.web_petvibe.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PetProfileService {

    @Autowired
    private final PetProfileRepository petProfileRepository;

    @Autowired
    private final AccountRepository accountRepository;

    // Lấy userId từ account đang đăng nhập
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername(); // username = phone
            Account account = accountRepository.findAccountByPhone(username);
            if (account == null) {
                throw new RuntimeException("Account not found");
            }
            return account.getId();
        } else if (principal instanceof String) {
            String username = (String) principal; // username = phone
            Account account = accountRepository.findAccountByPhone(username);
            if (account == null) {
                throw new RuntimeException("Account not found");
            }
            return account.getId();
        }

        throw new RuntimeException("Unable to get user information");
    }

    // Lấy tất cả pet profiles
    public List<PetProfile> getAllPetProfiles() {
        return petProfileRepository.findAllActive();
    }

    // Lấy pet profile theo ID
    public Optional<PetProfile> getPetProfileById(Long petId) {
        return petProfileRepository.findByIdActive(petId);
    }

    // Lấy pet profiles của user hiện tại
    public List<PetProfile> getMyPetProfiles() {
        Long currentUserId = getCurrentUserId();
        return petProfileRepository.findByUserIdActive(currentUserId);
    }

    // Lấy pet profiles theo user ID (cho admin)
    public List<PetProfile> getPetProfilesByUserId(Long userId) {
        return petProfileRepository.findByUserIdActive(userId);
    }

    // Lấy pet profiles theo pet type
    public List<PetProfile> getPetProfilesByPetType(String petType) {
        return petProfileRepository.findByPetTypeActive(petType);
    }

    // Tạo mới pet profile - TỰ ĐỘNG LẤY userId
    public PetProfile createPetProfile(PetProfile petProfile) {
        // Tự động set userId từ account đang đăng nhập
        Long currentUserId = getCurrentUserId();
        petProfile.setUserId(currentUserId);

        if (petProfile.getPetName() == null || petProfile.getPetName().trim().isEmpty()) {
            throw new RuntimeException("Pet name is required");
        }

        petProfile.setDeleted(false);
        return petProfileRepository.save(petProfile);
    }

    // Cập nhật pet profile
    public PetProfile updatePetProfile(Long petId, PetProfile petProfileDetails) {
        Optional<PetProfile> existingProfile = petProfileRepository.findByIdActive(petId);

        if (existingProfile.isPresent()) {
            PetProfile petProfile = existingProfile.get();

            // Kiểm tra xem pet profile có thuộc về user hiện tại không
            Long currentUserId = getCurrentUserId();
            if (!petProfile.getUserId().equals(currentUserId)) {
                throw new RuntimeException("You don't have permission to update this pet profile");
            }

            if (petProfileDetails.getPetName() != null) {
                petProfile.setPetName(petProfileDetails.getPetName());
            }
            if (petProfileDetails.getPetType() != null) {
                petProfile.setPetType(petProfileDetails.getPetType());
            }
            if (petProfileDetails.getBreed() != null) {
                petProfile.setBreed(petProfileDetails.getBreed());
            }
            if (petProfileDetails.getBirthDate() != null) {
                petProfile.setBirthDate(petProfileDetails.getBirthDate());
            }
            if (petProfileDetails.getWeight() != null) {
                petProfile.setWeight(petProfileDetails.getWeight());
            }
            if (petProfileDetails.getHealthNotes() != null) {
                petProfile.setHealthNotes(petProfileDetails.getHealthNotes());
            }
            if (petProfileDetails.getImageUrl() != null) {
                petProfile.setImageUrl(petProfileDetails.getImageUrl());
            }

            return petProfileRepository.save(petProfile);
        } else {
            throw new RuntimeException("Pet profile not found with id: " + petId);
        }
    }

    // Xóa mềm pet profile
    public void deletePetProfile(Long petId) {
        Optional<PetProfile> petProfile = petProfileRepository.findByIdActive(petId);

        if (petProfile.isPresent()) {
            PetProfile p = petProfile.get();

            // Kiểm tra xem pet profile có thuộc về user hiện tại không
            Long currentUserId = getCurrentUserId();
            if (!p.getUserId().equals(currentUserId)) {
                throw new RuntimeException("You don't have permission to delete this pet profile");
            }

            p.setDeleted(true);
            petProfileRepository.save(p);
        } else {
            throw new RuntimeException("Pet profile not found with id: " + petId);
        }
    }
}