package com.big_chat_brasil.api.domain.message.dto;

import com.big_chat_brasil.api.domain.enums.MessagePriority;

import java.util.UUID;

public record SendMessageCommand(UUID conversationId, String content, MessagePriority priority) {
}
