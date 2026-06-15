package com.big_chat_brasil.api.domain.conversation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    List<Conversation> findByClientIdOrderByUpdatedAtDesc(UUID clientId);

    Optional<Conversation> findByIdAndClientId(UUID id, UUID clientId);
}
