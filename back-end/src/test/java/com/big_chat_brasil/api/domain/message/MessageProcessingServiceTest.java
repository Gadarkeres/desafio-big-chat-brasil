package com.big_chat_brasil.api.domain.message;

import com.big_chat_brasil.api.domain.enums.MessageStatus;
import com.big_chat_brasil.api.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageProcessingServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageProcessingService messageProcessingService;

    @Test
    void shouldMarkQueuedMessageAsProcessing() {
        UUID messageId = UUID.randomUUID();
        Message message = message(messageId, MessageStatus.QUEUED);
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        boolean processed = messageProcessingService.markAsProcessing(messageId);

        assertTrue(processed);
        assertEquals(MessageStatus.PROCESSING, message.getStatus());
        assertNotNull(message.getProcessingAt());
        verify(messageRepository).save(message);
    }

    @Test
    void shouldIgnoreProcessingWhenMessageIsNotQueued() {
        UUID messageId = UUID.randomUUID();
        Message message = message(messageId, MessageStatus.SENT);
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        boolean processed = messageProcessingService.markAsProcessing(messageId);

        assertFalse(processed);
        assertEquals(MessageStatus.SENT, message.getStatus());
        verify(messageRepository, never()).save(message);
    }

    @Test
    void shouldMarkMessageAsSent() {
        UUID messageId = UUID.randomUUID();
        Message message = message(messageId, MessageStatus.PROCESSING);
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        messageProcessingService.markAsSent(messageId);

        assertEquals(MessageStatus.SENT, message.getStatus());
        assertNotNull(message.getSentAt());
        verify(messageRepository).save(message);
    }

    @Test
    void shouldMarkMessageAsFailedWithReason() {
        UUID messageId = UUID.randomUUID();
        Message message = message(messageId, MessageStatus.PROCESSING);
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        messageProcessingService.markAsFailed(messageId, "Erro simulado");

        assertEquals(MessageStatus.FAILED, message.getStatus());
        assertEquals("Erro simulado", message.getFailureReason());
        assertNotNull(message.getFailedAt());
        verify(messageRepository).save(message);
    }

    @Test
    void shouldThrowNotFoundWhenMessageDoesNotExist() {
        UUID messageId = UUID.randomUUID();
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> messageProcessingService.markAsSent(messageId));
    }

    private Message message(UUID id, MessageStatus status) {
        Message message = new Message();
        message.setId(id);
        message.setStatus(status);
        return message;
    }
}
