package com.kkimleang.chatservice.service;

import com.kkimleang.chatservice.client.ContentFeignClient;
import com.kkimleang.chatservice.client.UserFeignClient;
import com.kkimleang.chatservice.dto.*;
import com.kkimleang.chatservice.event.ChatDeletedEvent;
import com.kkimleang.chatservice.exception.*;
import com.kkimleang.chatservice.model.Chat;
import com.kkimleang.chatservice.repository.ChatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);
    private final ChatRepository chatRepository;
    private final UserFeignClient userFeignClient;
    private final ContentFeignClient contentFeignClient;
    private final KafkaTemplate<String, ChatDeletedEvent> kafkaTemplate;

    @Override
    public ChatResponse createChat(ChatRequest chat) {
        Chat newChat = new Chat();
        newChat.setTitle(chat.getTitle());
        newChat.setUserId(chat.getUserId());
        newChat = chatRepository.save(newChat);
        return ChatResponse.fromChat(newChat, null);
    }

    @Override
    public ChatResponse getChatById(String chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        Response<List<ContentResponse>> contents = contentFeignClient.getAllContentsByChatId(UUID.fromString(chatId));
        if (contents.isSuccess()) {
            return ChatResponse.fromChat(chat, contents.getPayload());
        } else {
            return ChatResponse.fromChat(chat, new ArrayList<>());
        }
    }

    @Override
    public ChatResponse updateChat(String chatId, ChatRequest chat) {
        Chat updateChat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        updateChat.setTitle(chat.getTitle());
        updateChat.setUserId(chat.getUserId());
        updateChat = chatRepository.save(updateChat);
        return ChatResponse.fromChat(updateChat, null);
    }

    @Override
    public Boolean deleteChat(String chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        Response<UserResponse> user = userFeignClient.getUserProfile(chat.getUserId());
        boolean isOwner = isUserIsOwnerOfChat(chat.getUserId());
        if (!isOwner) {
            throw new ResourceAccessDeniedException("You are not allowed to access this resource");
        }
        chatRepository.delete(chat);
        UserResponse userResponse = user.getPayload();
        ChatDeletedEvent chatDeletedEvent = new ChatDeletedEvent(
                chat.getId(),
                chat.getTitle(),
                userResponse.getUsername(),
                userResponse.getEmail(),
                chat.getCreatedAt().toString()
        );
        kafkaTemplate.send("chat-deleted", chatDeletedEvent);
        return true;
    }

    @Override
    public ChatResponse shareChat(String chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        chat.setShareId(UUID.randomUUID().toString());
        chat = chatRepository.save(chat);
        Response<List<ContentResponse>> contents = contentFeignClient.getAllContentsByChatId(UUID.fromString(chatId));
        if (contents.isSuccess()) {
            return ChatResponse.fromChat(chat, contents.getPayload());
        } else {
            return ChatResponse.fromChat(chat, null);
        }
    }

    @Override
    public ChatResponse unshareChat(String chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        chat.setShareId(null);
        chat = chatRepository.save(chat);
        return ChatResponse.fromChat(chat, null);
    }

    @Override
    public ChatResponse archiveChat(String chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        chat.setArchived(true);
        chat = chatRepository.save(chat);
        Response<List<ContentResponse>> contents = contentFeignClient.getAllContentsByChatId(UUID.fromString(chatId));
        if (contents.isSuccess()) {
            return ChatResponse.fromChat(chat, contents.getPayload());
        } else {
            return ChatResponse.fromChat(chat, null);
        }
    }

    @Override
    public ChatResponse unarchiveChat(String chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        chat.setArchived(false);
        chat = chatRepository.save(chat);
        return ChatResponse.fromChat(chat, null);
    }

    @Override
    public ChatResponse pinChat(String chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        chat.setPinned(true);
        chat = chatRepository.save(chat);
        return ChatResponse.fromChat(chat, null);
    }

    @Override
    public ChatResponse unpinChat(String chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        chat.setPinned(false);
        chat = chatRepository.save(chat);
        return ChatResponse.fromChat(chat, null);
    }

    @Transactional
    @Override
    public List<ChatResponse> getAllChatsByUserId(UUID userId) {
        boolean isOwner = isUserIsOwnerOfChat(userId);
        if (isOwner) {
            List<Chat> chats = chatRepository.findAllByUserId(userId);
            return ChatResponse.fromChatsWithoutContents(chats);
        } else {
            throw new ResourceAccessDeniedException("You are not allowed to access this resource");
        }
    }

    @Override
    public Response<List<ContentResponse>> getAllContentsByChatId(UUID chatId) {
        try {
            Chat chat = chatRepository.findById(chatId.toString()).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
            boolean isOwner = isUserIsOwnerOfChat(chat.getUserId());
            if (isOwner) {
                Response<List<ContentResponse>> contents = contentFeignClient.getAllContentsByChatId(chatId);
                if (contents.isSuccess()) {
                    return Response.<List<ContentResponse>>ok().setPayload(contents.getPayload());
                } else {
                    return Response.<List<ContentResponse>>exception().setErrors(contents.getErrors());
                }
            } else {
                return Response.<List<ContentResponse>>accessDenied().setErrors("You are not allowed to access this resource");
            }
        } catch (Exception e) {
            return Response.<List<ContentResponse>>exception().setErrors(e.getMessage());
        }
    }

    private boolean isUserIsOwnerOfChat(UUID userId) {
        Response<UserResponse> response = userFeignClient.getMyProfile();
        return response.isSuccess() && response.getPayload().getId().equals(userId);
    }
}
