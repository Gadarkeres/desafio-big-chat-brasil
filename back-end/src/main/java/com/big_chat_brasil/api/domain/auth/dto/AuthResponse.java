package com.big_chat_brasil.api.domain.auth.dto;

import com.big_chat_brasil.api.domain.client.Client;

public record AuthResponse(String token, ClientResponse client) {

    public static AuthResponse fromTokenAndClient(String token, Client client) {
        return new AuthResponse(token, ClientResponse.fromClient(client));
    }
}
