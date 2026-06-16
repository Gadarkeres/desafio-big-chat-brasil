package com.big_chat_brasil.api.domain.conversation;

import com.big_chat_brasil.api.domain.auth.CurrentClientService;
import com.big_chat_brasil.api.domain.client.Client;
import com.big_chat_brasil.api.domain.conversation.dto.ConversationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConversationController {

    private final CurrentClientService currentClientService;
    private final ConversationService conversationService;

    @Operation(
            summary = "Lista conversas do cliente autenticado",
            description = "Retorna as conversas vinculadas ao cliente identificado pelo token Bearer."
    )
    @ApiResponse(responseCode = "200", description = "Conversas retornadas")
    @ApiResponse(responseCode = "400", description = "Token ausente, expirado ou cliente inativo")
    @ApiResponse(responseCode = "404", description = "Token não encontrado")
    @GetMapping("/conversations")
    public List<ConversationResponse> findConversations(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        Client client = currentClientService.getAuthenticatedClient(authorizationHeader);
        return conversationService.findResponsesByClient(client);
    }
}
