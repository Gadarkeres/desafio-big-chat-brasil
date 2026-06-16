package com.big_chat_brasil.api.domain.message.dto;

import com.big_chat_brasil.api.domain.enums.MessagePriority;

import java.time.LocalDateTime;
import java.util.UUID;

public record QueuedMessage(UUID messageId, MessagePriority priority, LocalDateTime queuedAt, long sequence) {
}
