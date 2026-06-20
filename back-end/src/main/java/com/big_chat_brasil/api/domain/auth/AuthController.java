package com.big_chat_brasil.api.domain.auth;

import com.big_chat_brasil.api.domain.auth.dto.AuthRequest;
import com.big_chat_brasil.api.domain.auth.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Autentica um cliente por documento",
            description = "Identifica um cliente previamente cadastrado por CPF ou CNPJ e retorna um token simples para as proximas requisoções."
    )
    @ApiResponse(responseCode = "200", description = "Cliente autenticado")
    @ApiResponse(responseCode = "400", description = "Cliente inativo ou dados inválidos")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse authenticate(@Valid @RequestBody AuthRequest request) {
        return authService.authenticate(request.documentId(), request.documentType());
    }
}
