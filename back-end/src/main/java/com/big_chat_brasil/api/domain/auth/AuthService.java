package com.big_chat_brasil.api.domain.auth;

import com.big_chat_brasil.api.domain.auth.dto.AuthResponse;
import com.big_chat_brasil.api.domain.client.Client;
import com.big_chat_brasil.api.domain.client.ClientService;
import com.big_chat_brasil.api.domain.enums.DocumentType;
import com.big_chat_brasil.api.exception.NotFoundException;
import com.big_chat_brasil.api.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final int TOKEN_EXPIRATION_HOURS = 12;

    private final ClientService clientService;
    private final AuthTokenRepository authTokenRepository;

    @Transactional
    public AuthResponse authenticate(String documentId, DocumentType documentType) {
        Client client;

        try {
            client = clientService.findByDocument(documentId, documentType);
        } catch (NotFoundException exception) {
            log.warn("Tentativa de login com credenciais inválidas. documentType={}", documentType);
            throw new UnauthorizedException("Credenciais inválidas");
        }

        if (!Boolean.TRUE.equals(client.getActive())) {
            log.warn("Tentativa de login para cliente inativo. clientId={}", client.getId());
            throw new UnauthorizedException("Credenciais inválidas");
        }

        AuthToken authToken = new AuthToken();
        authToken.setClient(client);
        authToken.setToken(UUID.randomUUID().toString());
        authToken.setExpiresAt(LocalDateTime.now().plusHours(TOKEN_EXPIRATION_HOURS));

        AuthToken savedToken = authTokenRepository.save(authToken);
        log.info("Cliente autenticado com sucesso. clientId={} expiresAt={}", client.getId(), savedToken.getExpiresAt());

        return AuthResponse.fromTokenAndClient(savedToken.getToken(), client);
    }

    @Transactional(readOnly = true)
    public Client getClientByToken(String token) {
        AuthToken authToken = authTokenRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Token invalido"));

        if (authToken.getExpiresAt() != null && authToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Token expirado usado em requisição. clientId={}", authToken.getClient().getId());
            throw new UnauthorizedException("Token expirado");
        }

        if (!Boolean.TRUE.equals(authToken.getClient().getActive())) {
            log.warn("Token de cliente inativo usado em requisição. clientId={}", authToken.getClient().getId());
            throw new UnauthorizedException("Cliente inativo");
        }

        return authToken.getClient();
    }
}
