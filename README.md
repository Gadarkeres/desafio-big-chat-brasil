# BCB - Big Chat Brasil

Aplicação fullstack para o desafio técnico BCB. O projeto implementa uma plataforma simples de chat para empresas conversarem com seus clientes finais, com autenticação por documento, conversas, mensagens, fila de processamento, prioridade e validação financeira básica.

## Tecnologias Utilizadas

### Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Bean Validation
- PostgreSQL
- Flyway
- SpringDoc OpenAPI / Swagger
- Docker

### Frontend

- React
- TypeScript
- Vite
- Ant Design
- Axios
- TanStack Query
- React Router DOM
- js-cookie
- date-fns
- Docker + Nginx

## Funcionalidades Implementadas

### Backend

- API de autenticação por CPF/CNPJ.
- Geração e validação de token simples.
- Listagem de conversas do cliente autenticado.
- Listagem de mensagens de uma conversa.
- Envio de mensagens com prioridade normal ou urgente.
- Persistência em PostgreSQL.
- Migrations com Flyway.
- Validação financeira para clientes pré-pagos e pós-pagos.
- Registro de transações financeiras.
- Fila de mensagens com prioridade usando `PriorityBlockingQueue`.
- Worker em background para simular o processamento das mensagens.
- Ciclo de status da mensagem: enfileirada, processando, enviada, entregue, lida ou falha.
- Tratamento global de erros com respostas padronizadas.
- Documentação da API com Swagger.

### Frontend

- Tela de login por documento.
- Rotas privadas.
- Layout de chat responsivo para desktop e mobile.
- Lista de conversas navegável.
- Busca por nome do destinatário.
- Tela de mensagens com histórico.
- Envio de mensagens com prioridade normal ou urgente.
- Indicadores visuais de status da mensagem.
- Estados de carregamento e tela vazia.
- Feedbacks com notification do Ant Design.
- Modal com informações do cliente, plano, saldo ou limite.
- Formatação de datas e valores monetários.

### Integração

- Fluxo completo: login → listar conversas → abrir conversa → listar mensagens → enviar mensagem.
- Comunicação via REST API.
- Token enviado no header `Authorization`.
- Interceptor Axios para autenticação e tratamento de erros.
- Atualização dos dados com TanStack Query.
- Polling simples para atualizar mensagens e status sem WebSocket.

## Bônus Implementados

Além do mínimo solicitado, foram implementados:

- Persistência real em banco PostgreSQL.
- Validação financeira básica para pré-pago e pós-pago.
- Fila com prioridade normal/urgente.
- Status visual das mensagens no frontend.
- Busca/filtro por destinatário.
- Swagger/OpenAPI.
- Docker Compose para executar banco, backend e frontend.
- Seed demonstrativo com 10 conversas para apresentação.

## Como Rodar com Docker

Pré-requisitos:

- Docker
- Docker Compose

Na raiz do projeto, execute:

```bash
docker compose up --build
```

Serviços disponíveis:

- Frontend: `http://localhost:5173`
- Backend: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui.html`
- PostgreSQL: `localhost:5432`

Para rodar em segundo plano:

```bash
docker compose up --build -d
```

Para parar os containers:

```bash
docker compose down
```

Para remover também o volume do banco e recriar os dados do zero:

```bash
docker compose down -v
docker compose up --build
```

## Usuário de Demonstração

Use os dados abaixo na tela de login:

```json
{
  "documentId": "13829413904",
  "documentType": "CPF"
}
```

Esse cliente possui conversas e mensagens pré-cadastradas para facilitar a apresentação.

Também existe um cliente pós-pago seedado:

```json
{
  "documentId": "12345678000199",
  "documentType": "CNPJ"
}

