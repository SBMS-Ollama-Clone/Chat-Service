package com.kkimleang.chatservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class ChatRequest {
    private UUID userId;
    private String title;
    private String shareId;
}
