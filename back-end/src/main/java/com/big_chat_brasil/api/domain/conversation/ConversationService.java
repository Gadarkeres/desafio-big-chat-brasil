package com.big_chat_brasil.api.domain.conversation;

import com.big_chat_brasil.api.domain.client.Client;
import com.big_chat_brasil.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    @Transactional(readOnly = true)
    public List<Conversation> findByClient(Client client) {
        return conversationRepository.findByClientIdOrderByUpdatedAtDesc(client.getId());
    }

    @Transactional(readOnly = true)
    public Conversation findByIdAndClient(UUID conversationId, Client client) {
        return conversationRepository.findByIdAndClientId(conversationId, client.getId())
                .orElseThrow(() -> new NotFoundException("Conversa não encontrada"));
    }
}
