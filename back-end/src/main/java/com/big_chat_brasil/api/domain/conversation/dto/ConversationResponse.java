package com.big_chat_brasil.api.domain.conversation.dto;

import com.big_chat_brasil.api.domain.conversation.Conversation;
import com.big_chat_brasil.api.domain.message.Message;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConversationResponse(
        UUID id,
        UUID recipientId,
        String recipientName,
        String lastMessageContent,
        LocalDateTime lastMessageTime,
        int unreadCount
) {

    public static ConversationResponse from(Conversation conversation, Message lastMessage) {
        return new ConversationResponse(
                conversation.getId(),
                conversation.getRecipient().getId(),
                conversation.getRecipient().getName(),
                lastMessage == null ? "" : lastMessage.getContent(),
                lastMessage == null ? conversation.getCreatedAt() : lastMessage.getQueuedAt(),
                0
        );
    }
}
