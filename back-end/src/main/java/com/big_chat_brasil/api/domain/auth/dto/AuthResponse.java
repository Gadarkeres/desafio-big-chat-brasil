package com.big_chat_brasil.api.domain.auth.dto;

import com.big_chat_brasil.api.domain.client.Client;

import java.math.BigDecimal;
import java.util.UUID;

public record AuthResponse(String token, ClientResponse client) {

    public static AuthResponse from(AuthResult authResult) {
        return new AuthResponse(authResult.token(), ClientResponse.from(authResult.client()));
    }

    public record ClientResponse(
            UUID id,
            String name,
            String documentId,
            String documentType,
            BigDecimal balance,
            BigDecimal limit,
            String planType,
            Boolean active
    ) {

        public static ClientResponse from(Client client) {
            return new ClientResponse(
                    client.getId(),
                    client.getName(),
                    client.getDocumentId(),
                    client.getDocumentType().name(),
                    client.getBalance(),
                    client.getMonthlyLimit(),
                    client.getPlanType().name().toLowerCase(),
                    client.getActive()
            );
        }
    }
}
