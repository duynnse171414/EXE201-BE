package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.AiImageAnalysis;
import com.example.web_petvibe.repository.AiImageAnalysisRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AiImageAnalysisService {

    @Autowired
    private final AiImageAnalysisRepository aiImageAnalysisRepository;

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta}")
    private String geminiApiUrl;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String geminiModel;

    // Lấy tất cả analysis
    public List<AiImageAnalysis> getAllAnalyses() {
        return aiImageAnalysisRepository.findAllActive();
    }

    // Lấy analysis theo ID
    public Optional<AiImageAnalysis> getAnalysisById(Long analysisId) {
        return aiImageAnalysisRepository.findByIdActive(analysisId);
    }

    // Lấy analysis theo User ID
    public List<AiImageAnalysis> getAnalysesByUserId(Long userId) {
        return aiImageAnalysisRepository.findByUserIdActive(userId);
    }

    // Lấy analysis theo Pet ID
    public List<AiImageAnalysis> getAnalysesByPetId(Long petId) {
        return aiImageAnalysisRepository.findByPetIdActive(petId);
    }

    // Đếm số lượng analysis của user
    public Long countAnalysesByUserId(Long userId) {
        return aiImageAnalysisRepository.countByUserId(userId);
    }

    // Phân tích hình ảnh thú cưng bằng Google Gemini (MIỄN PHÍ!)
    public AiImageAnalysis analyzeImageWithAI(Long userId, Long petId, String imageUrl) {
        try {
            // Gọi Google Gemini Vision API
            Map<String, Object> analysisData = callGeminiVision(imageUrl);

            // Tạo đối tượng analysis mới
            AiImageAnalysis analysis = new AiImageAnalysis();
            analysis.setUserId(userId);
            analysis.setPetId(petId);
            analysis.setImageUrl(imageUrl);
            analysis.setAnalysisResult((String) analysisData.get("analysisResult"));
            analysis.setRecommendations((String) analysisData.get("recommendations"));
            analysis.setDetectedItems((String) analysisData.get("detectedItems"));
            analysis.setAiModelVersion(geminiModel);
            analysis.setAnalyzedAt(LocalDateTime.now());
            analysis.setIsDeleted(false);

            return aiImageAnalysisRepository.save(analysis);

        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze image: " + e.getMessage(), e);
        }
    }

    // Gọi Google Gemini Vision API (MIỄN PHÍ!)
    private Map<String, Object> callGeminiVision(String imageUrl) {
        try {
            // Validate API key
            if (geminiApiKey == null || geminiApiKey.isEmpty() || geminiApiKey.equals("your-gemini-api-key-here")) {
                throw new RuntimeException("Gemini API key is not configured. Please add your API key to application.properties");
            }

            // Download image và convert sang base64
            String base64Image = downloadAndEncodeImage(imageUrl);

            // Tạo request body cho Gemini
            Map<String, Object> requestBody = new HashMap<>();

            List<Map<String, Object>> contents = new ArrayList<>();
            Map<String, Object> content = new HashMap<>();

            List<Map<String, Object>> parts = new ArrayList<>();

            // Text prompt
            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", buildAnalysisPrompt());
            parts.add(textPart);

            // Image data
            Map<String, Object> imagePart = new HashMap<>();
            Map<String, Object> inlineData = new HashMap<>();
            inlineData.put("mimeType", "image/jpeg");
            inlineData.put("data", base64Image);
            imagePart.put("inline_data", inlineData);
            parts.add(imagePart);

            content.put("parts", parts);
            contents.add(content);
            requestBody.put("contents", contents);

            // Tạo headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Gọi API với key trong URL
            String urlWithKey = geminiApiUrl + "/models/" + geminiModel + ":generateContent?key=" + geminiApiKey;

            ResponseEntity<String> response = restTemplate.exchange(
                    urlWithKey,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Parse response
            return parseGeminiResponse(response.getBody());

        } catch (Exception e) {
            throw new RuntimeException("Gemini API call failed: " + e.getMessage(), e);
        }
    }

    // Download image và convert sang base64
    private String downloadAndEncodeImage(String imageUrl) {
        try {
            // Set headers để tránh bị chặn
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    imageUrl,
                    HttpMethod.GET,
                    entity,
                    byte[].class
            );

            if (response.getBody() == null || response.getBody().length == 0) {
                throw new RuntimeException("Image data is empty");
            }

            byte[] imageBytes = response.getBody();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download image from URL: " + imageUrl + ". Error: " + e.getMessage(), e);
        }
    }

    // Tạo prompt phân tích
    private String buildAnalysisPrompt() {
        return "Bạn là chuyên gia chăm sóc thú cưng. Hãy phân tích hình ảnh này và cung cấp:\n" +
                "1. PHÂN TÍCH: Mô tả chi tiết về thú cưng (giống, màu sắc, kích thước, tư thế, biểu cảm, trạng thái sức khỏe có thể nhìn thấy)\n" +
                "2. KHUYẾN NGHỊ: Đưa ra các khuyến nghị về chăm sóc, dinh dưỡng, vận động và sức khỏe dựa trên những gì nhìn thấy\n" +
                "3. PHÁT HIỆN: Liệt kê các vật phẩm, môi trường hoặc đặc điểm quan trọng trong ảnh (đồ chơi, bát ăn, nơi ở, vv)\n\n" +
                "Vui lòng trả lời bằng tiếng Việt và theo format JSON CHÍNH XÁC:\n" +
                "{\n" +
                "  \"analysis\": \"Mô tả chi tiết về thú cưng\",\n" +
                "  \"recommendations\": \"Các khuyến nghị chăm sóc\",\n" +
                "  \"detected_items\": [\"item1\", \"item2\", \"item3\"]\n" +
                "}";
    }

    // Parse Gemini response
    private Map<String, Object> parseGeminiResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);

            // Lấy text từ response
            String content = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            // Remove markdown code blocks nếu có
            content = content.replaceAll("```json\\s*", "").replaceAll("```\\s*$", "").trim();

            // Parse JSON từ content
            JsonNode analysisJson = objectMapper.readTree(content);

            Map<String, Object> result = new HashMap<>();
            result.put("analysisResult", analysisJson.path("analysis").asText());
            result.put("recommendations", analysisJson.path("recommendations").asText());

            // Convert detected_items array to JSON string
            JsonNode detectedItems = analysisJson.path("detected_items");
            result.put("detectedItems", objectMapper.writeValueAsString(detectedItems));

            return result;

        } catch (Exception e) {
            // Fallback nếu parse lỗi
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("analysisResult", "Không thể phân tích hình ảnh. Vui lòng đảm bảo URL hình ảnh hợp lệ và có thể truy cập được.");
            fallback.put("recommendations", "Vui lòng thử lại với hình ảnh rõ ràng hơn hoặc liên hệ hỗ trợ.");
            fallback.put("detectedItems", "[]");
            return fallback;
        }
    }

    // Tạo mới analysis (manual)
    public AiImageAnalysis createAnalysis(AiImageAnalysis analysis) {
        if (analysis.getUserId() == null) {
            throw new RuntimeException("User ID is required");
        }
        if (analysis.getPetId() == null) {
            throw new RuntimeException("Pet ID is required");
        }
        if (analysis.getImageUrl() == null || analysis.getImageUrl().isEmpty()) {
            throw new RuntimeException("Image URL is required");
        }
        analysis.setIsDeleted(false);
        return aiImageAnalysisRepository.save(analysis);
    }

    // Cập nhật analysis
    public AiImageAnalysis updateAnalysis(Long analysisId, AiImageAnalysis updatedAnalysis) {
        Optional<AiImageAnalysis> existingAnalysis = aiImageAnalysisRepository.findByIdActive(analysisId);
        if (existingAnalysis.isPresent()) {
            AiImageAnalysis analysis = existingAnalysis.get();

            if (updatedAnalysis.getAnalysisResult() != null) {
                analysis.setAnalysisResult(updatedAnalysis.getAnalysisResult());
            }
            if (updatedAnalysis.getRecommendations() != null) {
                analysis.setRecommendations(updatedAnalysis.getRecommendations());
            }
            if (updatedAnalysis.getDetectedItems() != null) {
                analysis.setDetectedItems(updatedAnalysis.getDetectedItems());
            }
            if (updatedAnalysis.getAiModelVersion() != null) {
                analysis.setAiModelVersion(updatedAnalysis.getAiModelVersion());
            }

            return aiImageAnalysisRepository.save(analysis);
        } else {
            throw new RuntimeException("Analysis not found with id: " + analysisId);
        }
    }

    // Xóa mềm analysis
    public void deleteAnalysis(Long analysisId) {
        Optional<AiImageAnalysis> analysis = aiImageAnalysisRepository.findByIdActive(analysisId);
        if (analysis.isPresent()) {
            AiImageAnalysis a = analysis.get();
            a.setIsDeleted(true);
            aiImageAnalysisRepository.save(a);
        } else {
            throw new RuntimeException("Analysis not found with id: " + analysisId);
        }
    }
}