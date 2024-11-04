package com.kkimleang.chatservice.repository;

import com.kkimleang.chatservice.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {
    @Query("SELECT c FROM Chat c WHERE c.userId = ?1 ORDER BY c.createdAt DESC")
    List<Chat> findAllByUserId(UUID userId);
}
