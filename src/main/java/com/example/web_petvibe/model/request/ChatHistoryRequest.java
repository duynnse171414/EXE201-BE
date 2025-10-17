package com.example.web_petvibe.model.request;

import com.example.web_petvibe.entity.ChatHistory.ChatType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    private String userMessage;

    private String aiResponse;

    @NotNull(message = "Chat type is required")
    private ChatType chatType;

    private Map<String, Object> contextData;
}