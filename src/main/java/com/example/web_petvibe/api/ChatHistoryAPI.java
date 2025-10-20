package com.example.web_petvibe.api;

import com.example.web_petvibe.entity.ChatHistory;
import com.example.web_petvibe.model.request.ChatHistoryRequest;
import com.example.web_petvibe.model.response.ApiResponse;
import com.example.web_petvibe.model.response.ChatHistoryResponse;
import com.example.web_petvibe.service.ChatHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat-history")
@SecurityRequirement(name = "api")
@Tag(name = "Chat History", description = "API quản lý lịch sử chat với AI")
public class ChatHistoryAPI {

    @Autowired
    private ChatHistoryService chatHistoryService;

    // POST chat with AI - Customer only
    @PostMapping("/chat")
    @Operation(summary = "Chat với AI trợ lý PetVibe",
            description = "Sử dụng Google Gemini để chat và tự động lưu lịch sử")
    public ResponseEntity<?> chatWithAI(@RequestBody ChatHistoryRequest request) {
        try {
            if (request.getUserId() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("User ID is required", false));
            }
            if (request.getUserMessage() == null || request.getUserMessage().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("User message is required", false));
            }
            if (request.getChatType() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Chat type is required", false));
            }

            ChatHistory chatHistory = chatHistoryService.chatWithAI(
                    request.getUserId(),
                    request.getUserMessage(),
                    request.getChatType(),
                    request.getContextData()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ChatHistoryResponse("Chat completed successfully", true, chatHistory));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Chat failed: " + e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Internal server error: " + e.getMessage(), false));
        }
    }

    // GET all chat history - Admin and Staff only
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(summary = "Lấy tất cả lịch sử chat")
    public ResponseEntity<List<ChatHistory>> getAllChatHistory() {
        try {
            return ResponseEntity.ok(chatHistoryService.getAllChatHistory());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET chat history by id - All authenticated users
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF', 'CUSTOMER')")
    @Operation(summary = "Lấy lịch sử chat theo ID")
    public ResponseEntity<?> getChatHistoryById(@PathVariable Long id) {
        Optional<ChatHistory> chatHistory = chatHistoryService.getChatHistoryById(id);
        return chatHistory.isPresent()
                ? ResponseEntity.ok(new ChatHistoryResponse("Chat history found successfully", true, chatHistory.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Chat history not found with id: " + id, false));
    }

    // GET chat history by user id - Customer (own data) or Admin/Staff (any user)
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF', 'CUSTOMER')")
    @Operation(summary = "Lấy lịch sử chat theo User ID")
    public ResponseEntity<?> getChatHistoryByUserId(@PathVariable Long userId) {
        try {
            List<ChatHistory> chatHistories = chatHistoryService.getChatHistoryByUserId(userId);
            return ResponseEntity.ok(chatHistories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error fetching chat history: " + e.getMessage(), false));
        }
    }

    // GET chat history by user id and chat type - Customer (own data) or Admin/Staff (any user)
    @GetMapping("/user/{userId}/type/{chatType}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF', 'CUSTOMER')")
    @Operation(summary = "Lấy lịch sử chat theo User ID và Chat Type")
    public ResponseEntity<?> getChatHistoryByUserIdAndType(
            @PathVariable Long userId,
            @PathVariable ChatHistory.ChatType chatType) {
        try {
            List<ChatHistory> chatHistories = chatHistoryService.getChatHistoryByUserIdAndType(userId, chatType);
            return ResponseEntity.ok(chatHistories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error fetching chat history: " + e.getMessage(), false));
        }
    }

    // GET chat history by chat type - Admin and Staff only
    @GetMapping("/type/{chatType}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(summary = "Lấy lịch sử chat theo Chat Type")
    public ResponseEntity<?> getChatHistoryByType(@PathVariable ChatHistory.ChatType chatType) {
        try {
            List<ChatHistory> chatHistories = chatHistoryService.getChatHistoryByType(chatType);
            return ResponseEntity.ok(chatHistories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error fetching chat history: " + e.getMessage(), false));
        }
    }

    // GET count chats by user - All authenticated users
    @GetMapping("/user/{userId}/count")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF', 'CUSTOMER')")
    @Operation(summary = "Đếm số lượng chat của user")
    public ResponseEntity<?> countChatsByUserId(@PathVariable Long userId) {
        try {
            Long count = chatHistoryService.countChatsByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Total chats: " + count, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error counting chats: " + e.getMessage(), false));
        }
    }

    // POST create chat history (manual) - Admin and Staff only
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(summary = "Tạo lịch sử chat thủ công (không dùng AI)")
    public ResponseEntity<?> createChatHistory(@RequestBody ChatHistory chatHistory) {
        try {
            ChatHistory created = chatHistoryService.createChatHistory(chatHistory);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ChatHistoryResponse("Chat history created successfully", true, created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // PUT update chat history - Admin and Staff only
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(summary = "Cập nhật lịch sử chat")
    public ResponseEntity<?> updateChatHistory(@PathVariable Long id, @RequestBody ChatHistory chatHistory) {
        try {
            ChatHistory updated = chatHistoryService.updateChatHistory(id, chatHistory);
            return ResponseEntity.ok(new ChatHistoryResponse("Chat history updated successfully", true, updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // DELETE chat history - Admin only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Xóa lịch sử chat")
    public ResponseEntity<?> deleteChatHistory(@PathVariable Long id) {
        try {
            chatHistoryService.deleteChatHistory(id);
            return ResponseEntity.ok(new ApiResponse("Chat history deleted successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }
}