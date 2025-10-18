package com.example.web_petvibe.model.request;

import com.example.web_petvibe.entity.ChatHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryRequest {

    private Long userId;

    private String userMessage;

    private ChatHistory.ChatType chatType;

    private Map<String, Object> contextData;
}