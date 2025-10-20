package com.example.web_petvibe.api;

import com.example.web_petvibe.entity.PetProfile;
import com.example.web_petvibe.model.response.ApiResponse;
import com.example.web_petvibe.model.response.PetProfileResponse;
import com.example.web_petvibe.service.PetProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pet-profiles")
@SecurityRequirement(name = "api")
public class PetProfileAPI {

    @Autowired
    private PetProfileService petProfileService;

    // GET all pet profiles (cho admin)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<List<PetProfile>> getAllPetProfiles() {
        try {
            return ResponseEntity.ok(petProfileService.getAllPetProfiles());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET pet profiles của user hiện tại
    @GetMapping("/my-pets")
    public ResponseEntity<List<PetProfile>> getMyPetProfiles() {
        try {
            List<PetProfile> profiles = petProfileService.getMyPetProfiles();
            return ResponseEntity.ok(profiles);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET pet profile by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getPetProfileById(@PathVariable Long id) {
        Optional<PetProfile> petProfile = petProfileService.getPetProfileById(id);
        return petProfile.isPresent()
                ? ResponseEntity.ok(new PetProfileResponse("Pet profile found successfully", true, petProfile.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Pet profile not found with id: " + id, false));
    }

    // GET pet profiles by user id (cho admin)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PetProfile>> getPetProfilesByUserId(@PathVariable Long userId) {
        try {
            List<PetProfile> profiles = petProfileService.getPetProfilesByUserId(userId);
            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET pet profiles by pet type
    @GetMapping("/type/{petType}")
    public ResponseEntity<List<PetProfile>> getPetProfilesByPetType(@PathVariable String petType) {
        try {
            List<PetProfile> profiles = petProfileService.getPetProfilesByPetType(petType);
            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST create pet profile - TỰ ĐỘNG LẤY userId, KHÔNG CẦN GỬI userId
    @PostMapping
    public ResponseEntity<?> createPetProfile(@RequestBody PetProfile petProfile) {
        try {
            // userId sẽ được tự động set trong service
            PetProfile created = petProfileService.createPetProfile(petProfile);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new PetProfileResponse("Pet profile created successfully", true, created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // PUT update pet profile
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePetProfile(@PathVariable Long id, @RequestBody PetProfile petProfile) {
        try {
            PetProfile updated = petProfileService.updatePetProfile(id, petProfile);
            return ResponseEntity.ok(new PetProfileResponse("Pet profile updated successfully", true, updated));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("permission")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage(), false));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }

    // DELETE pet profile
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePetProfile(@PathVariable Long id) {
        try {
            petProfileService.deletePetProfile(id);
            return ResponseEntity.ok(new ApiResponse("Pet profile deleted successfully", true));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("permission")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage(), false));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }
}