package com.big_chat_brasil.api.domain.auth;

import com.big_chat_brasil.api.domain.client.Client;
import com.big_chat_brasil.api.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentClientService {

    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthService authService;

    public Client getAuthenticatedClient(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new BadRequestException("Token de autenticação não informado");
        }

        return authService.getClientByToken(authorizationHeader.substring(BEARER_PREFIX.length()));
    }
}
