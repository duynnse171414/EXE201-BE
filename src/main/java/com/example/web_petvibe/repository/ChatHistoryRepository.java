package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.ChatHistory;
import com.example.web_petvibe.entity.ChatHistory.ChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    // Tìm tất cả chat history của một user
    @Query("SELECT c FROM ChatHistory c WHERE c.userId = ?1 ORDER BY c.createdAt DESC")
    List<ChatHistory> findByUserId(Long userId);

    // Tìm chat history theo user và chat type
    @Query("SELECT c FROM ChatHistory c WHERE c.userId = ?1 AND c.chatType = ?2 ORDER BY c.createdAt DESC")
    List<ChatHistory> findByUserIdAndChatType(Long userId, ChatType chatType);

    // Tìm chat history trong khoảng thời gian
    @Query("SELECT c FROM ChatHistory c WHERE c.userId = ?1 AND c.createdAt BETWEEN ?2 AND ?3 ORDER BY c.createdAt DESC")
    List<ChatHistory> findByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    // Tìm chat history gần nhất của user (giới hạn số lượng)
    @Query("SELECT c FROM ChatHistory c WHERE c.userId = ?1 ORDER BY c.createdAt DESC")
    List<ChatHistory> findRecentChatsByUserId(Long userId);
}