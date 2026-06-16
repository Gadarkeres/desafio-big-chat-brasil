package com.big_chat_brasil.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bigChatBrasilOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("BCB - Big Chat Brasil API")
                        .description("API para autenticacao, conversas, mensagens, fila e validacao financeira basica.")
                        .version("v1"));
    }
}
