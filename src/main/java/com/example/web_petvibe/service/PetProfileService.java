package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.PetProfile;
import com.example.web_petvibe.entity.Account;
import com.example.web_petvibe.model.response.PetProfileDetailResponse;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PetProfileService {

    @Autowired
    private final PetProfileRepository petProfileRepository;

    @Autowired
    private final AccountRepository accountRepository;

    // Lấy Account đang đăng nhập
    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new RuntimeException("Unable to get user information");
        }

        Account account = accountRepository.findAccountByPhone(username);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }
        return account;
    }

    private Long getCurrentUserId() {
        return getCurrentAccount().getId();
    }

    // Map PetProfile sang PetProfileDetailResponse
    private PetProfileDetailResponse mapToDetailResponse(PetProfile petProfile) {
        PetProfileDetailResponse response = new PetProfileDetailResponse();
        response.setPetId(petProfile.getPetId());
        response.setUserId(petProfile.getAccount().getId());
        response.setFullName(petProfile.getAccount().getFullName());
        response.setPetName(petProfile.getPetName());
        response.setPetType(petProfile.getPetType());
        response.setBreed(petProfile.getBreed());
        response.setBirthDate(petProfile.getBirthDate());
        response.setWeight(petProfile.getWeight());
        response.setPetAge(petProfile.getPetAge());
        response.setPetSize(petProfile.getPetSize());
        response.setHealthNotes(petProfile.getHealthNotes());
        response.setImageUrl(petProfile.getImageUrl());
        response.setCreatedAt(petProfile.getCreatedAt());
        return response;
    }

    // Lấy tất cả pet profiles
    public List<PetProfileDetailResponse> getAllPetProfiles() {
        return petProfileRepository.findAllActive()
                .stream()
                .map(this::mapToDetailResponse)
                .collect(Collectors.toList());
    }

    // Lấy pet profile theo ID
    public Optional<PetProfileDetailResponse> getPetProfileById(Long petId) {
        return petProfileRepository.findByIdActive(petId)
                .map(this::mapToDetailResponse);
    }

    // Lấy pet profiles của user hiện tại
    public List<PetProfileDetailResponse> getMyPetProfiles() {
        Long currentUserId = getCurrentUserId();
        return petProfileRepository.findByUserIdActive(currentUserId)
                .stream()
                .map(this::mapToDetailResponse)
                .collect(Collectors.toList());
    }

    // Lấy pet profiles theo user ID (cho admin)
    public List<PetProfileDetailResponse> getPetProfilesByUserId(Long userId) {
        return petProfileRepository.findByUserIdActive(userId)
                .stream()
                .map(this::mapToDetailResponse)
                .collect(Collectors.toList());
    }

    // Lấy pet profiles theo pet type
    public List<PetProfileDetailResponse> getPetProfilesByPetType(String petType) {
        return petProfileRepository.findByPetTypeActive(petType)
                .stream()
                .map(this::mapToDetailResponse)
                .collect(Collectors.toList());
    }

    // Tạo mới pet profile - TỰ ĐỘNG LẤY Account và set quan hệ
    public PetProfileDetailResponse createPetProfile(PetProfile petProfile) {
        Account currentAccount = getCurrentAccount();
        petProfile.setAccount(currentAccount);

        if (petProfile.getPetName() == null || petProfile.getPetName().trim().isEmpty()) {
            throw new RuntimeException("Pet name is required");
        }

        petProfile.setDeleted(false);
        PetProfile saved = petProfileRepository.save(petProfile);
        return mapToDetailResponse(saved);
    }

    // Cập nhật pet profile
    public PetProfileDetailResponse updatePetProfile(Long petId, PetProfile petProfileDetails) {
        Optional<PetProfile> existingProfile = petProfileRepository.findByIdActive(petId);

        if (existingProfile.isPresent()) {
            PetProfile petProfile = existingProfile.get();

            Long currentUserId = getCurrentUserId();
            if (petProfile.getAccount().getId() != currentUserId) {
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
            if (petProfileDetails.getPetAge() != null) {
                petProfile.setPetAge(petProfileDetails.getPetAge());
            }
            if (petProfileDetails.getPetSize() != null) {
                petProfile.setPetSize(petProfileDetails.getPetSize());
            }

            PetProfile updated = petProfileRepository.save(petProfile);
            return mapToDetailResponse(updated);
        } else {
            throw new RuntimeException("Pet profile not found with id: " + petId);
        }
    }

    // Xóa mềm pet profile
    public void deletePetProfile(Long petId) {
        Optional<PetProfile> petProfile = petProfileRepository.findByIdActive(petId);

        if (petProfile.isPresent()) {
            PetProfile p = petProfile.get();

            Long currentUserId = getCurrentUserId();
            if (p.getAccount().getId() != currentUserId) {
                throw new RuntimeException("You don't have permission to delete this pet profile");
            }

            p.setDeleted(true);
            petProfileRepository.save(p);
        } else {
            throw new RuntimeException("Pet profile not found with id: " + petId);
        }
    }
}