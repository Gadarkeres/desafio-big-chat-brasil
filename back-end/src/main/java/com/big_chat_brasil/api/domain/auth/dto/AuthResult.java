package com.big_chat_brasil.api.domain.auth.dto;

import com.big_chat_brasil.api.domain.client.Client;

public record AuthResult(String token, Client client){
}
