package com.big_chat_brasil.api.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessagePriority {
    NORMAL,
    URGENT;

    @JsonCreator
    public static MessagePriority fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return MessagePriority.valueOf(value.trim().toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
