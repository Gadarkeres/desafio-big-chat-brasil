package com.big_chat_brasil.api.domain.auth.dto;

import com.big_chat_brasil.api.domain.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthRequest(
        @NotBlank String documentId,
        @NotNull DocumentType documentType
) {
}
