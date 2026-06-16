package com.big_chat_brasil.api.domain.message.dto;

import com.big_chat_brasil.api.domain.client.Client;
import com.big_chat_brasil.api.domain.enums.MessagePriority;
import com.big_chat_brasil.api.domain.enums.PlanType;
import com.big_chat_brasil.api.domain.message.Message;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SendMessageResponse(
        UUID id,
        String status,
        LocalDateTime timestamp,
        LocalDateTime estimatedDelivery,
        BigDecimal cost,
        BigDecimal currentBalance
) {

    public static SendMessageResponse from(Message message, Client client) {
        return new SendMessageResponse(
                message.getId(),
                message.getStatus().name().toLowerCase(),
                message.getQueuedAt(),
                message.getQueuedAt().plusSeconds(message.getPriority() == MessagePriority.URGENT ? 5 : 15),
                message.getCost(),
                client.getPlanType() == PlanType.PREPAID ? client.getBalance() : null
        );
    }
}
