package com.big_chat_brasil.api.domain.message.dto;

import com.big_chat_brasil.api.domain.enums.MessagePriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SendMessageRequest(
        @NotNull UUID conversationId,
        UUID recipientId,
        @NotBlank String content,
        @NotNull MessagePriority priority
) {
}
