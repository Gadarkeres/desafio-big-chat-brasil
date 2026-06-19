package com.big_chat_brasil.api.domain.message;

import com.big_chat_brasil.api.domain.auth.CurrentClientService;
import com.big_chat_brasil.api.domain.client.Client;
import com.big_chat_brasil.api.domain.message.dto.MessageResponse;
import com.big_chat_brasil.api.domain.message.dto.SendMessageCommand;
import com.big_chat_brasil.api.domain.message.dto.SendMessageRequest;
import com.big_chat_brasil.api.domain.message.dto.SendMessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final CurrentClientService currentClientService;
    private final MessageService messageService;

    @Operation(
            summary = "Lista mensagens de uma conversa",
            description = "Retorna o historico de mensagens de uma conversa pertencente ao cliente autenticado."
    )
    @ApiResponse(responseCode = "200", description = "Mensagens retornadas")
    @ApiResponse(responseCode = "400", description = "Token ausente, expirado ou cliente inativo")
    @ApiResponse(responseCode = "404", description = "Token ou conversa não encontrada")
    @GetMapping("/conversations/{conversationId}/messages")
    public List<MessageResponse> findMessages(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable UUID conversationId
    ) {
        Client client = currentClientService.getAuthenticatedClient(authorizationHeader);

        return messageService.findByConversation(conversationId, client)
                .stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Operation(
            summary = "Envia uma mensagem",
            description = "Valida saldo ou limite do cliente, registra a mensagem como queued, cria transação financeira e adiciona a mensagem na fila."
    )
    @ApiResponse(responseCode = "201", description = "Mensagem enfileirada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos, saldo insuficiente ou limite insuficiente")
    @ApiResponse(responseCode = "404", description = "Token ou conversa não encontrada")
    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public SendMessageResponse sendMessage(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody SendMessageRequest request
    ) {
        Client client = currentClientService.getAuthenticatedClient(authorizationHeader);
        Message message = messageService.sendMessage(
                client,
                new SendMessageCommand(request.conversationId(), request.content(), request.priority())
        );

        return SendMessageResponse.from(message, client);
    }
}
