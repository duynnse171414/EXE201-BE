package com.example.web_petvibe.model.response;

import com.example.web_petvibe.entity.ChatHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryResponse {

    private String message;

    private Boolean success;

    private ChatHistory data;
}