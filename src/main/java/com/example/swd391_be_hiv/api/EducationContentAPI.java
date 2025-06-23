package com.example.swd391_be_hiv.api;

import com.example.swd391_be_hiv.model.reponse.EducationContentResponse;
import com.example.swd391_be_hiv.model.request.EducationContentRequest;
import com.example.swd391_be_hiv.service.EducationContentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@SecurityRequirement(name = "api")
@RestController
@RequestMapping("/api/education-content")
public class EducationContentAPI {

    @Autowired
    EducationContentService educationContentService;

    @PostMapping
    public ResponseEntity<EducationContentResponse> createEducationContent(@Valid @RequestBody EducationContentRequest requestDTO) {
        EducationContentResponse response = educationContentService.createEducationContent(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("getEducationContents")
    public ResponseEntity<List<EducationContentResponse>> getEducationContents() {
        List<EducationContentResponse> contentList = educationContentService.getAllEducationContents();
        return ResponseEntity.ok(contentList);
    }

    @GetMapping("{id}")
    public ResponseEntity<EducationContentResponse> getEducationContentById(@PathVariable Long id) {
        EducationContentResponse response = educationContentService.getEducationContentById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<EducationContentResponse> updateEducationContent(@Valid @RequestBody EducationContentRequest requestDTO, @PathVariable Long id) {
        EducationContentResponse response = educationContentService.updateEducationContent(requestDTO, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<EducationContentResponse> deleteEducationContent(@PathVariable Long id) {
        EducationContentResponse response = educationContentService.deleteEducationContent(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<EducationContentResponse>> getEducationContentsByStaff(@PathVariable Long staffId) {
        List<EducationContentResponse> contents = educationContentService.getEducationContentsByStaff(staffId);
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<EducationContentResponse>> getEducationContentsByTitle(@RequestParam String keyword) {
        List<EducationContentResponse> contents = educationContentService.getEducationContentsByTitleContaining(keyword);
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/search/content")
    public ResponseEntity<List<EducationContentResponse>> getEducationContentsByContent(@RequestParam String keyword) {
        List<EducationContentResponse> contents = educationContentService.getEducationContentsByContentContaining(keyword);
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<EducationContentResponse>> getEducationContentsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<EducationContentResponse> contents = educationContentService.getEducationContentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(contents);
    }
}