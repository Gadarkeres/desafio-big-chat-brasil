package com.big_chat_brasil.api.domain.message;

import com.big_chat_brasil.api.domain.enums.MessagePriority;
import com.big_chat_brasil.api.domain.message.dto.QueuedMessage;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageQueueServiceTest {

    private final MessageQueueService messageQueueService = new MessageQueueService();

    @Test
    void shouldProcessUrgentMessagesBeforeNormalMessages() {
        LocalDateTime now = LocalDateTime.now();
        Message normalMessage = message(MessagePriority.NORMAL, now.minusMinutes(10));
        Message urgentMessage = message(MessagePriority.URGENT, now);

        messageQueueService.enqueue(normalMessage);
        messageQueueService.enqueue(urgentMessage);

        assertEquals(urgentMessage.getId(), messageQueueService.dequeue().orElseThrow().messageId());
        assertEquals(normalMessage.getId(), messageQueueService.dequeue().orElseThrow().messageId());
    }

    @Test
    void shouldKeepFifoOrderWhenMessagesHaveSamePriorityAndTime() {
        LocalDateTime queuedAt = LocalDateTime.now();
        Message firstMessage = message(MessagePriority.NORMAL, queuedAt);
        Message secondMessage = message(MessagePriority.NORMAL, queuedAt);

        messageQueueService.enqueue(firstMessage);
        messageQueueService.enqueue(secondMessage);

        assertEquals(firstMessage.getId(), messageQueueService.dequeue().orElseThrow().messageId());
        assertEquals(secondMessage.getId(), messageQueueService.dequeue().orElseThrow().messageId());
    }

    @Test
    void shouldReturnEmptyWhenQueueHasNoMessages() {
        Optional<QueuedMessage> queuedMessage = messageQueueService.dequeue();

        assertTrue(queuedMessage.isEmpty());
    }

    private Message message(MessagePriority priority, LocalDateTime queuedAt) {
        Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setPriority(priority);
        message.setQueuedAt(queuedAt);
        return message;
    }
}
