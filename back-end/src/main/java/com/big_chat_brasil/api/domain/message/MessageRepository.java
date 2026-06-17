package com.big_chat_brasil.api.domain.message;

import com.big_chat_brasil.api.domain.enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByConversationIdOrderByQueuedAtAsc(UUID conversationId);

    Message findFirstByConversationIdOrderByQueuedAtDesc(UUID conversationId);

    List<Message> findByStatusOrderByQueuedAtAsc(MessageStatus status);
}
