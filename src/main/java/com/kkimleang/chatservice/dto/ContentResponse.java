package com.kkimleang.chatservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class ContentResponse {
    private String contentId;
    private String chatId;
    private String modelName;
    private String message;
    private String messageType;
    private String createdAt;
    private String updatedAt;
}
