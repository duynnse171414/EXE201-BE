package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.ChatHistory;
import com.example.web_petvibe.repository.ChatHistoryRepository;
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
public class ChatHistoryService {

    @Autowired
    private final ChatHistoryRepository chatHistoryRepository;

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta}")
    private String geminiApiUrl;

    @Value("${gemini.model:gemini-1.5-flash}")
    private String geminiModel;

    // Lấy tất cả chat history
    public List<ChatHistory> getAllChatHistory() {
        return chatHistoryRepository.findAllActive();
    }

    // Lấy chat history theo ID
    public Optional<ChatHistory> getChatHistoryById(Long chatId) {
        return chatHistoryRepository.findByIdActive(chatId);
    }

    // Lấy chat history theo User ID
    public List<ChatHistory> getChatHistoryByUserId(Long userId) {
        return chatHistoryRepository.findByUserIdActive(userId);
    }

    // Lấy chat history theo User ID và Chat Type
    public List<ChatHistory> getChatHistoryByUserIdAndType(Long userId, ChatHistory.ChatType chatType) {
        return chatHistoryRepository.findByUserIdAndChatTypeActive(userId, chatType);
    }

    // Lấy chat history theo Chat Type
    public List<ChatHistory> getChatHistoryByType(ChatHistory.ChatType chatType) {
        return chatHistoryRepository.findByChatTypeActive(chatType);
    }

    // Đếm số lượng chat của user
    public Long countChatsByUserId(Long userId) {
        return chatHistoryRepository.countByUserId(userId);
    }

    // Chat với AI sử dụng Google Gemini (MIỄN PHÍ!)
    public ChatHistory chatWithAI(Long userId, String userMessage, ChatHistory.ChatType chatType, Map<String, Object> contextData) {
        try {
            // Gọi Google Gemini API
            String aiResponse = callGeminiChat(userMessage, chatType, contextData);

            // Tạo đối tượng chat history mới
            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setUserId(userId);
            chatHistory.setUserMessage(userMessage);
            chatHistory.setAiResponse(aiResponse);
            chatHistory.setChatType(chatType);
            chatHistory.setContextData(contextData);
            chatHistory.setCreatedAt(LocalDateTime.now());
            chatHistory.setDeleted(false);

            return chatHistoryRepository.save(chatHistory);

        } catch (Exception e) {
            throw new RuntimeException("Failed to chat with AI: " + e.getMessage(), e);
        }
    }

    // Gọi Google Gemini Chat API (MIỄN PHÍ!)
    private String callGeminiChat(String userMessage, ChatHistory.ChatType chatType, Map<String, Object> contextData) {
        try {
            // Validate API key
            if (geminiApiKey == null || geminiApiKey.isEmpty() || geminiApiKey.equals("your-gemini-api-key-here")) {
                throw new RuntimeException("Gemini API key is not configured. Please add your API key to application.properties");
            }

            // Tạo system prompt dựa trên chat type
            String systemPrompt = buildSystemPrompt(chatType);

            // Tạo request body cho Gemini
            Map<String, Object> requestBody = new HashMap<>();

            List<Map<String, Object>> contents = new ArrayList<>();
            Map<String, Object> content = new HashMap<>();

            List<Map<String, Object>> parts = new ArrayList<>();

            // System instruction + User message
            Map<String, Object> textPart = new HashMap<>();
            String fullPrompt = systemPrompt + "\n\nNgười dùng hỏi: " + userMessage;

            // Thêm context data nếu có
            if (contextData != null && !contextData.isEmpty()) {
                fullPrompt += "\n\nThông tin bổ sung: " + objectMapper.writeValueAsString(contextData);
            }

            textPart.put("text", fullPrompt);
            parts.add(textPart);

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
            return parseGeminiChatResponse(response.getBody());

        } catch (Exception e) {
            throw new RuntimeException("Gemini API call failed: " + e.getMessage(), e);
        }
    }

    // Tạo system prompt dựa trên chat type
    private String buildSystemPrompt(ChatHistory.ChatType chatType) {
        switch (chatType) {
            case general:
                return "Bạn là trợ lý AI thân thiện của PetVibe - nền tảng chăm sóc thú cưng. " +
                        "Hãy trả lời các câu hỏi chung về thú cưng, chăm sóc, và cung cấp thông tin hữu ích. " +
                        "Trả lời bằng tiếng Việt, thân thiện và dễ hiểu.";

            case product_inquiry:
                return "Bạn là chuyên gia tư vấn sản phẩm thú cưng của PetVibe. " +
                        "Hãy giúp khách hàng tìm hiểu về sản phẩm, so sánh, và đưa ra khuyến nghị phù hợp. " +
                        "Trả lời bằng tiếng Việt, chuyên nghiệp và chi tiết về tính năng, giá cả, ưu nhược điểm.";

            case order_support:
                return "Bạn là nhân viên hỗ trợ đơn hàng của PetVibe. " +
                        "Hãy giúp khách hàng về các vấn đề: theo dõi đơn hàng, giao hàng, thanh toán, đổi trả. " +
                        "Trả lời bằng tiếng Việt, lịch sự, và cung cấp hướng dẫn cụ thể.";

            default:
                return "Bạn là trợ lý AI của PetVibe. Hãy trả lời câu hỏi bằng tiếng Việt một cách hữu ích.";
        }
    }

    // Parse Gemini chat response
    private String parseGeminiChatResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);

            // Lấy text từ response
            String content = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            return content;

        } catch (Exception e) {
            return "Xin lỗi, tôi không thể xử lý yêu cầu của bạn lúc này. Vui lòng thử lại sau.";
        }
    }

    // Tạo mới chat history (manual)
    public ChatHistory createChatHistory(ChatHistory chatHistory) {
        if (chatHistory.getUserId() == null) {
            throw new RuntimeException("User ID is required");
        }
        if (chatHistory.getUserMessage() == null || chatHistory.getUserMessage().isEmpty()) {
            throw new RuntimeException("User message is required");
        }
        if (chatHistory.getChatType() == null) {
            throw new RuntimeException("Chat type is required");
        }
        chatHistory.setDeleted(false);
        return chatHistoryRepository.save(chatHistory);
    }

    // Cập nhật chat history
    public ChatHistory updateChatHistory(Long chatId, ChatHistory updatedChatHistory) {
        Optional<ChatHistory> existingChatHistory = chatHistoryRepository.findByIdActive(chatId);
        if (existingChatHistory.isPresent()) {
            ChatHistory chatHistory = existingChatHistory.get();

            if (updatedChatHistory.getUserMessage() != null) {
                chatHistory.setUserMessage(updatedChatHistory.getUserMessage());
            }
            if (updatedChatHistory.getAiResponse() != null) {
                chatHistory.setAiResponse(updatedChatHistory.getAiResponse());
            }
            if (updatedChatHistory.getChatType() != null) {
                chatHistory.setChatType(updatedChatHistory.getChatType());
            }
            if (updatedChatHistory.getContextData() != null) {
                chatHistory.setContextData(updatedChatHistory.getContextData());
            }

            return chatHistoryRepository.save(chatHistory);
        } else {
            throw new RuntimeException("Chat history not found with id: " + chatId);
        }
    }

    // Xóa mềm chat history
    public void deleteChatHistory(Long chatId) {
        Optional<ChatHistory> chatHistory = chatHistoryRepository.findByIdActive(chatId);
        if (chatHistory.isPresent()) {
            ChatHistory c = chatHistory.get();
            c.setDeleted(true);
            chatHistoryRepository.save(c);
        } else {
            throw new RuntimeException("Chat history not found with id: " + chatId);
        }
    }
}