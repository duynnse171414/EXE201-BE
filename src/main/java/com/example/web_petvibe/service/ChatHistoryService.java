package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.ChatHistory;
import com.example.web_petvibe.entity.ChatHistory.ChatType;
import com.example.web_petvibe.repository.ChatHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatHistoryService {

    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    public List<ChatHistory> getAllChatHistory() {
        return chatHistoryRepository.findAll();
    }

    public Optional<ChatHistory> getChatHistoryById(Long id) {
        return chatHistoryRepository.findById(id);
    }

    public List<ChatHistory> getChatHistoryByUserId(Long userId) {
        return chatHistoryRepository.findByUserId(userId);
    }

    public List<ChatHistory> getChatHistoryByUserIdAndType(Long userId, ChatType chatType) {
        return chatHistoryRepository.findByUserIdAndChatType(userId, chatType);
    }

    public List<ChatHistory> getChatHistoryByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return chatHistoryRepository.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    public ChatHistory createChatHistory(ChatHistory chatHistory) {
        if (chatHistory.getUserId() == null) {
            throw new RuntimeException("User ID is required");
        }

        if (chatHistory.getChatType() == null) {
            throw new RuntimeException("Chat type is required");
        }

        return chatHistoryRepository.save(chatHistory);
    }

    public ChatHistory updateChatHistory(Long id, ChatHistory updatedChatHistory) {
        Optional<ChatHistory> chatOpt = chatHistoryRepository.findById(id);

        if (!chatOpt.isPresent()) {
            throw new RuntimeException("Chat history not found with id: " + id);
        }

        ChatHistory chat = chatOpt.get();

        if (updatedChatHistory.getUserMessage() != null) {
            chat.setUserMessage(updatedChatHistory.getUserMessage());
        }
        if (updatedChatHistory.getAiResponse() != null) {
            chat.setAiResponse(updatedChatHistory.getAiResponse());
        }
        if (updatedChatHistory.getChatType() != null) {
            chat.setChatType(updatedChatHistory.getChatType());
        }
        if (updatedChatHistory.getContextData() != null) {
            chat.setContextData(updatedChatHistory.getContextData());
        }

        return chatHistoryRepository.save(chat);
    }

    public void deleteChatHistory(Long id) {
        Optional<ChatHistory> chat = chatHistoryRepository.findById(id);
        if (chat.isPresent()) {
            chatHistoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Chat history not found with id: " + id);
        }
    }

    public void deleteAllChatHistoryByUserId(Long userId) {
        List<ChatHistory> chatHistories = chatHistoryRepository.findByUserId(userId);
        if (!chatHistories.isEmpty()) {
            chatHistoryRepository.deleteAll(chatHistories);
        } else {
            throw new RuntimeException("No chat history found for user id: " + userId);
        }
    }
}