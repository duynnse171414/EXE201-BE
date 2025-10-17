package com.example.web_petvibe.api;

import com.example.web_petvibe.entity.ChatHistory;
import com.example.web_petvibe.entity.ChatHistory.ChatType;
import com.example.web_petvibe.model.request.ChatHistoryRequest;
import com.example.web_petvibe.model.response.ApiResponse;
import com.example.web_petvibe.model.response.GetChatHistoryResponse;
import com.example.web_petvibe.service.ChatHistoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat-history")
@SecurityRequirement(name = "api")
public class ChatHistoryAPI {

    @Autowired
    private ChatHistoryService chatHistoryService;

    @GetMapping("/getAll")
    public ResponseEntity<List<ChatHistory>> getAllChatHistory() {
        try {
            return ResponseEntity.ok(chatHistoryService.getAllChatHistory());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChatHistoryById(@PathVariable Long id) {
        Optional<ChatHistory> chat = chatHistoryService.getChatHistoryById(id);
        if (chat.isPresent()) {
            ChatHistory c = chat.get();
            GetChatHistoryResponse response = new GetChatHistoryResponse(
                    "Chat history found successfully",
                    true,
                    c.getChatId(),
                    c.getUserId(),
                    c.getUserMessage(),
                    c.getAiResponse(),
                    c.getChatType(),
                    c.getCreatedAt(),
                    c.getContextData()
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Chat history not found with id: " + id, false));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getChatHistoryByUserId(@PathVariable Long userId) {
        try {
            List<ChatHistory> chatHistories = chatHistoryService.getChatHistoryByUserId(userId);
            if (chatHistories.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse("No chat history found for user id: " + userId, true));
            }
            return ResponseEntity.ok(chatHistories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error retrieving chat history", false));
        }
    }

    @GetMapping("/user/{userId}/type/{chatType}")
    public ResponseEntity<?> getChatHistoryByUserIdAndType(
            @PathVariable Long userId,
            @PathVariable ChatType chatType) {
        try {
            List<ChatHistory> chatHistories = chatHistoryService.getChatHistoryByUserIdAndType(userId, chatType);
            if (chatHistories.isEmpty()) {
                return ResponseEntity.ok(
                        new ApiResponse("No chat history found for user id: " + userId + " and type: " + chatType, true)
                );
            }
            return ResponseEntity.ok(chatHistories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error retrieving chat history", false));
        }
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<?> getChatHistoryByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<ChatHistory> chatHistories = chatHistoryService.getChatHistoryByDateRange(userId, startDate, endDate);
            if (chatHistories.isEmpty()) {
                return ResponseEntity.ok(
                        new ApiResponse("No chat history found in the specified date range", true)
                );
            }
            return ResponseEntity.ok(chatHistories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error retrieving chat history", false));
        }
    }

    @PostMapping
    public ResponseEntity<?> createChatHistory(@Valid @RequestBody ChatHistoryRequest request) {
        try {
            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setUserId(request.getUserId());
            chatHistory.setUserMessage(request.getUserMessage());
            chatHistory.setAiResponse(request.getAiResponse());
            chatHistory.setChatType(request.getChatType());
            chatHistory.setContextData(request.getContextData());

            ChatHistory created = chatHistoryService.createChatHistory(chatHistory);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Chat history created successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateChatHistory(
            @PathVariable Long id,
            @Valid @RequestBody ChatHistoryRequest request) {
        try {
            ChatHistory updatedChatHistory = new ChatHistory();
            updatedChatHistory.setUserMessage(request.getUserMessage());
            updatedChatHistory.setAiResponse(request.getAiResponse());
            updatedChatHistory.setChatType(request.getChatType());
            updatedChatHistory.setContextData(request.getContextData());

            ChatHistory result = chatHistoryService.updateChatHistory(id, updatedChatHistory);
            return ResponseEntity.ok(new ApiResponse("Chat history updated successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChatHistory(@PathVariable Long id) {
        try {
            chatHistoryService.deleteChatHistory(id);
            return ResponseEntity.ok(new ApiResponse("Chat history deleted successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteAllChatHistoryByUserId(@PathVariable Long userId) {
        try {
            chatHistoryService.deleteAllChatHistoryByUserId(userId);
            return ResponseEntity.ok(
                    new ApiResponse("All chat history deleted successfully for user id: " + userId, true)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }
}