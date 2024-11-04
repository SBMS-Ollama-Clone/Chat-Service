package com.kkimleang.chatservice.dto;

import com.kkimleang.chatservice.model.Chat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class ChatResponse {
    private String chatId;
    private UUID userId;
    private String title;
    private List<ContentResponse> contents;
    private String createdAt;
    private String updatedAt;
    private String shareId;
    private boolean isPinned;
    private boolean isArchived;

    public static ChatResponse fromChat(Chat chat, List<ContentResponse> contents) {
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setChatId(chat.getId());
        chatResponse.setUserId(chat.getUserId());
        chatResponse.setTitle(chat.getTitle());
        chatResponse.setContents(contents);
        chatResponse.setCreatedAt(chat.getCreatedAt().toString());
        chatResponse.setUpdatedAt(chat.getUpdatedAt().toString());
        chatResponse.setShareId(chat.getShareId());
        chatResponse.setPinned(chat.isPinned());
        chatResponse.setArchived(chat.isArchived());
        return chatResponse;
    }

    public static List<ChatResponse> fromChats(List<Chat> chats, List<List<ContentResponse>> contents) {
        return chats.stream().map(chat -> fromChat(chat, contents.get(chats.indexOf(chat)))).collect(Collectors.toList());
    }

    public static List<ChatResponse> fromChatsWithoutContents(List<Chat> chats) {
        return chats.stream().map(ChatResponse::fromChatWithoutContent).collect(Collectors.toList());
    }

    private static ChatResponse fromChatWithoutContent(Chat chat) {
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setChatId(chat.getId());
        chatResponse.setUserId(chat.getUserId());
        chatResponse.setTitle(chat.getTitle());
        chatResponse.setCreatedAt(chat.getCreatedAt().toString());
        chatResponse.setUpdatedAt(chat.getUpdatedAt().toString());
        chatResponse.setShareId(chat.getShareId());
        chatResponse.setPinned(chat.isPinned());
        chatResponse.setArchived(chat.isArchived());
        return chatResponse;
    }
}
