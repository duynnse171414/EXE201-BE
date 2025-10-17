package com.example.web_petvibe.api;

import com.example.web_petvibe.entity.AiImageAnalysis;
import com.example.web_petvibe.model.request.AnalyzeImageRequest;
import com.example.web_petvibe.model.response.ApiResponse;
import com.example.web_petvibe.model.response.AiImageAnalysisResponse;
import com.example.web_petvibe.service.AiImageAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ai-image-analysis")
@SecurityRequirement(name = "api")
@Tag(name = "AI Image Analysis", description = "API phân tích hình ảnh thú cưng bằng AI")
public class AiImageAnalysisAPI {

    @Autowired
    private AiImageAnalysisService aiImageAnalysisService;

    // POST analyze image with AI
    @PostMapping("/analyze")
    @Operation(summary = "Phân tích hình ảnh thú cưng bằng AI",
            description = "Sử dụng OpenAI Vision để phân tích hình ảnh và đưa ra khuyến nghị")
    public ResponseEntity<?> analyzeImage(@RequestBody AnalyzeImageRequest request) {
        try {
            if (request.getUserId() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("User ID is required", false));
            }
            if (request.getPetId() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Pet ID is required", false));
            }
            if (request.getImageUrl() == null || request.getImageUrl().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Image URL is required", false));
            }

            AiImageAnalysis analysis = aiImageAnalysisService.analyzeImageWithAI(
                    request.getUserId(),
                    request.getPetId(),
                    request.getImageUrl()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AiImageAnalysisResponse("Image analyzed successfully", true, analysis));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Analysis failed: " + e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Internal server error: " + e.getMessage(), false));
        }
    }

    // GET all analyses
    @GetMapping("/getAll")
    @Operation(summary = "Lấy tất cả phân tích")
    public ResponseEntity<List<AiImageAnalysis>> getAllAnalyses() {
        try {
            return ResponseEntity.ok(aiImageAnalysisService.getAllAnalyses());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET analysis by id
    @GetMapping("/{id}")
    @Operation(summary = "Lấy phân tích theo ID")
    public ResponseEntity<?> getAnalysisById(@PathVariable Long id) {
        Optional<AiImageAnalysis> analysis = aiImageAnalysisService.getAnalysisById(id);
        return analysis.isPresent()
                ? ResponseEntity.ok(new AiImageAnalysisResponse("Analysis found successfully", true, analysis.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Analysis not found with id: " + id, false));
    }

    // GET analyses by user id
    @GetMapping("/user/{userId}")
    @Operation(summary = "Lấy phân tích theo User ID")
    public ResponseEntity<?> getAnalysesByUserId(@PathVariable Long userId) {
        try {
            List<AiImageAnalysis> analyses = aiImageAnalysisService.getAnalysesByUserId(userId);
            return ResponseEntity.ok(analyses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error fetching analyses: " + e.getMessage(), false));
        }
    }

    // GET analyses by pet id
    @GetMapping("/pet/{petId}")
    @Operation(summary = "Lấy phân tích theo Pet ID")
    public ResponseEntity<?> getAnalysesByPetId(@PathVariable Long petId) {
        try {
            List<AiImageAnalysis> analyses = aiImageAnalysisService.getAnalysesByPetId(petId);
            return ResponseEntity.ok(analyses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error fetching analyses: " + e.getMessage(), false));
        }
    }

    // GET count analyses by user
    @GetMapping("/user/{userId}/count")
    @Operation(summary = "Đếm số lượng phân tích của user")
    public ResponseEntity<?> countAnalysesByUserId(@PathVariable Long userId) {
        try {
            Long count = aiImageAnalysisService.countAnalysesByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Total analyses: " + count, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error counting analyses: " + e.getMessage(), false));
        }
    }

    // POST create analysis (manual)
    @PostMapping
    @Operation(summary = "Tạo phân tích thủ công (không dùng AI)")
    public ResponseEntity<?> createAnalysis(@RequestBody AiImageAnalysis analysis) {
        try {
            AiImageAnalysis created = aiImageAnalysisService.createAnalysis(analysis);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AiImageAnalysisResponse("Analysis created successfully", true, created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // PUT update analysis
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật phân tích")
    public ResponseEntity<?> updateAnalysis(@PathVariable Long id, @RequestBody AiImageAnalysis analysis) {
        try {
            AiImageAnalysis updated = aiImageAnalysisService.updateAnalysis(id, analysis);
            return ResponseEntity.ok(new AiImageAnalysisResponse("Analysis updated successfully", true, updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // DELETE analysis
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa phân tích")
    public ResponseEntity<?> deleteAnalysis(@PathVariable Long id) {
        try {
            aiImageAnalysisService.deleteAnalysis(id);
            return ResponseEntity.ok(new ApiResponse("Analysis deleted successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }
}