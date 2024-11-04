package com.kkimleang.chatservice.service;

import com.kkimleang.chatservice.dto.ChatRequest;
import com.kkimleang.chatservice.dto.ChatResponse;
import com.kkimleang.chatservice.dto.ContentResponse;
import com.kkimleang.chatservice.dto.Response;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatResponse createChat(ChatRequest chat);
    ChatResponse getChatById(String chatId);
    ChatResponse updateChat(String chatId, ChatRequest chat);
    Boolean deleteChat(String chatId);
    ChatResponse shareChat(String chatId);
    ChatResponse unshareChat(String chatId);
    ChatResponse archiveChat(String chatId);
    ChatResponse unarchiveChat(String chatId);
    ChatResponse pinChat(String chatId);
    ChatResponse unpinChat(String chatId);

    List<ChatResponse> getAllChatsByUserId(UUID userId);

    Response<List<ContentResponse>> getAllContentsByChatId(UUID chatId);
}
