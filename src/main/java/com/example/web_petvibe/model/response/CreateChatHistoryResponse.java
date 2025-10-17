
package com.example.web_petvibe.model.response;

import com.example.web_petvibe.entity.ChatHistory;
import com.example.web_petvibe.entity.ChatHistory.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatHistoryResponse {
    private String message;
    private boolean success;
    private ChatHistory data;
}