package com.kkimleang.chatservice.controller;

import com.kkimleang.chatservice.client.UserFeignClient;
import com.kkimleang.chatservice.dto.*;
import com.kkimleang.chatservice.exception.*;
import com.kkimleang.chatservice.model.Chat;
import com.kkimleang.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final UserFeignClient userFeignClient;
    private final ChatService chatService;

    @GetMapping("/of-users/{userId}")
    public Response<List<ChatResponse>> getAllChats(@PathVariable UUID userId) {
        try {
            return Response.<List<ChatResponse>>ok().setPayload(chatService.getAllChatsByUserId(userId));
        } catch (ResourceAccessDeniedException e) {
            return Response.<List<ChatResponse>>accessDenied().setErrors(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return Response.<List<ChatResponse>>notFound().setErrors(e.getMessage());
        } catch (Exception e) {
            return Response.<List<ChatResponse>>exception().setErrors(e.getMessage());
        }
    }

    @PostMapping
    public Response<ChatResponse> createChat(@RequestBody ChatRequest chatRequest) {
        return Response.<ChatResponse>created().setPayload(chatService.createChat(chatRequest));
    }

    @DeleteMapping("/{chatId}/delete")
    public Response<ChatResponse> deleteChat(@PathVariable String chatId) {
        try {
            Boolean isDeleted = chatService.deleteChat(chatId);
            if (isDeleted) {
                return Response.<ChatResponse>ok().setPayload(new ChatResponse());
            } else {
                return Response.<ChatResponse>exception().setErrors("Failed to delete chat with " + chatId);
            }
        } catch (Exception e) {
            return Response.<ChatResponse>exception().setErrors(e.getMessage());
        }
    }

    @PutMapping("/{chatId}/rename")
    public Response<ChatResponse> renameChat(@PathVariable String chatId, @RequestBody ChatRequest chatRequest) {
        try {
            ChatResponse updatedChat = chatService.updateChat(chatId, chatRequest);
            if (updatedChat == null) {
                return Response.<ChatResponse>exception().setErrors("Failed to rename chat with " + chatId);
            } else {
                return Response.<ChatResponse>ok().setPayload(updatedChat);
            }
        } catch (Exception e) {
            return Response.<ChatResponse>exception().setErrors(e.getMessage());
        }
    }

    @GetMapping("/{chatId}/contents")
    public Response<List<ContentResponse>> getAllContentsByChatId(@PathVariable UUID chatId) {
        try {
            Response<UserResponse> user = userFeignClient.getMyProfile();
            if (user == null || user.getPayload() == null) {
                return Response.<List<ContentResponse>>notFound().setErrors("User is not found");
            }
            ChatResponse chat = chatService.getChatById(chatId.toString());
            if (chat == null) {
                return Response.<List<ContentResponse>>notFound().setErrors("Chat with " + chatId + " is not found");
            }
            if (!Objects.equals(chat.getUserId(), user.getPayload().getId())) {
                return Response.<List<ContentResponse>>accessDenied().setErrors("You are not allowed to view contents of chat with " + chatId);
            }
            Response<List<ContentResponse>> contents = chatService.getAllContentsByChatId(chatId);
            if (contents == null || contents.getPayload() == null) {
                return Response.<List<ContentResponse>>notFound().setErrors("Contents of chat with " + chatId + " are not found");
            } else {
                log.info("Contents of chat with {} are found", chatId);
                return Response.<List<ContentResponse>>ok().setPayload(contents.getPayload());
            }
        } catch (Exception e) {
            return Response.<List<ContentResponse>>exception().setErrors(e.getMessage());
        }
    }
}
