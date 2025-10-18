package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    @Query("SELECT c FROM ChatHistory c WHERE c.isDeleted = false ORDER BY c.createdAt DESC")
    List<ChatHistory> findAllActive();

    @Query("SELECT c FROM ChatHistory c WHERE c.chatId = ?1 AND c.isDeleted = false")
    Optional<ChatHistory> findByIdActive(Long chatId);

    @Query("SELECT c FROM ChatHistory c WHERE c.userId = ?1 AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<ChatHistory> findByUserIdActive(Long userId);

    @Query("SELECT c FROM ChatHistory c WHERE c.userId = ?1 AND c.chatType = ?2 AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<ChatHistory> findByUserIdAndChatTypeActive(Long userId, ChatHistory.ChatType chatType);

    @Query("SELECT COUNT(c) FROM ChatHistory c WHERE c.userId = ?1 AND c.isDeleted = false")
    Long countByUserId(Long userId);

    @Query("SELECT c FROM ChatHistory c WHERE c.chatType = ?1 AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<ChatHistory> findByChatTypeActive(ChatHistory.ChatType chatType);
}