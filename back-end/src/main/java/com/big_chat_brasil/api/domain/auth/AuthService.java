package com.big_chat_brasil.api.domain.auth;

import com.big_chat_brasil.api.domain.auth.dto.AuthResult;
import com.big_chat_brasil.api.domain.client.Client;
import com.big_chat_brasil.api.domain.client.ClientService;
import com.big_chat_brasil.api.domain.enums.DocumentType;
import com.big_chat_brasil.api.exception.BadRequestException;
import com.big_chat_brasil.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int TOKEN_EXPIRATION_HOURS = 12;

    private final ClientService clientService;
    private final AuthTokenRepository authTokenRepository;

    @Transactional
    public AuthResult authenticate(String documentId, DocumentType documentType) {
        Client client = clientService.findByDocument(documentId, documentType);

        if (!Boolean.TRUE.equals(client.getActive())) {
            throw new BadRequestException("Cliente inativo");
        }

        AuthToken authToken = new AuthToken();
        authToken.setClient(client);
        authToken.setToken(UUID.randomUUID().toString());
        authToken.setExpiresAt(LocalDateTime.now().plusHours(TOKEN_EXPIRATION_HOURS));

        AuthToken savedToken = authTokenRepository.save(authToken);
        return new AuthResult(savedToken.getToken(), client);
    }

    @Transactional(readOnly = true)
    public Client getClientByToken(String token) {
        AuthToken authToken = authTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Token inválido"));

        if (authToken.getExpiresAt() != null && authToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token expirado");
        }

        if (!Boolean.TRUE.equals(authToken.getClient().getActive())) {
            throw new BadRequestException("Cliente inativo");
        }

        return authToken.getClient();
    }
}
