package com.kkimleang.chatservice.controller;

import com.kkimleang.chatservice.dto.ChatRequest;
import com.kkimleang.chatservice.dto.ChatResponse;
import com.kkimleang.chatservice.dto.ContentResponse;
import com.kkimleang.chatservice.dto.Response;
import com.kkimleang.chatservice.exception.*;
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

    @DeleteMapping("/{chatId}/delete/of-users/{userId}")
    public Response<ChatResponse> deleteChat(@PathVariable String chatId, @PathVariable UUID userId) {
        try {
            ChatResponse chatResponse = chatService.getChatById(chatId);
            if (chatResponse == null) {
                return Response.<ChatResponse>notFound().setErrors("Chat with " + chatId + " not found");
            } else {
                if (!Objects.equals(chatResponse.getUserId(), userId)) {
                    return Response.<ChatResponse>accessDenied().setErrors("You are not allowed to delete chat with " + chatId);
                }
                Boolean isDeleted = chatService.deleteChat(chatId);
                if (isDeleted) {
                    return Response.<ChatResponse>ok().setPayload(chatResponse);
                } else {
                    return Response.<ChatResponse>exception().setErrors("Failed to delete chat with " + chatId);
                }
            }
        } catch (Exception e) {
            return Response.<ChatResponse>exception().setErrors(e.getMessage());
        }
    }

    @PutMapping("/{chatId}/rename/of-users/{userId}")
    public Response<ChatResponse> renameChat(@PathVariable String chatId, @PathVariable UUID userId, @RequestBody ChatRequest chatRequest) {
        try {
            chatRequest.setUserId(userId);
            ChatResponse chatResponse = chatService.getChatById(chatId);
            if (chatResponse == null) {
                return Response.<ChatResponse>notFound().setErrors("Chat with " + chatId + " not found");
            } else {
                if (!Objects.equals(chatResponse.getUserId(), userId)) {
                    return Response.<ChatResponse>accessDenied().setErrors("You are not allowed to rename chat with " + chatId);
                }
                ChatResponse updatedChat = chatService.updateChat(chatId, chatRequest);
                if (updatedChat == null) {
                    return Response.<ChatResponse>exception().setErrors("Failed to rename chat with " + chatId);
                } else {
                    return Response.<ChatResponse>ok().setPayload(updatedChat);
                }
            }
        } catch (Exception e) {
            return Response.<ChatResponse>exception().setErrors(e.getMessage());
        }
    }

    @GetMapping("/{chatId}/contents")
    public Response<List<ContentResponse>> getAllContentsByChatId(@PathVariable UUID chatId) {
        try {
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
