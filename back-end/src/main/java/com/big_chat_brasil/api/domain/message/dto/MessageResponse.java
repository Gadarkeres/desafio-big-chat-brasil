package com.big_chat_brasil.api.domain.message.dto;

import com.big_chat_brasil.api.domain.message.Message;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID conversationId,
        String content,
        SentByResponse sentBy,
        LocalDateTime timestamp,
        String priority,
        String status,
        BigDecimal cost
) {

    public static MessageResponse fromMessage(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getConversation().getId(),
                message.getContent(),
                new SentByResponse(message.getSentById(), message.getSentByType().name().toLowerCase()),
                message.getQueuedAt(),
                message.getPriority().name().toLowerCase(),
                message.getStatus().name().toLowerCase(),
                message.getCost()
        );
    }

    public record SentByResponse(UUID id, String type) {
    }
}
