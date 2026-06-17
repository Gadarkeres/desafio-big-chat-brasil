package com.big_chat_brasil.api.domain.message;

import com.big_chat_brasil.api.domain.enums.MessageStatus;
import com.big_chat_brasil.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageProcessingService {

    private final MessageRepository messageRepository;

    @Transactional
    public boolean markAsProcessing(UUID messageId) {
        Message message = findById(messageId);

        if (message.getStatus() != MessageStatus.QUEUED) {
            return false;
        }

        message.setStatus(MessageStatus.PROCESSING);
        message.setProcessingAt(LocalDateTime.now());
        messageRepository.save(message);

        return true;
    }

    @Transactional
    public void markAsSent(UUID messageId) {
        Message message = findById(messageId);
        message.setStatus(MessageStatus.SENT);
        message.setSentAt(LocalDateTime.now());
        messageRepository.save(message);
    }

    @Transactional
    public void markAsFailed(UUID messageId, String reason) {
        Message message = findById(messageId);
        message.setStatus(MessageStatus.FAILED);
        message.setFailedAt(LocalDateTime.now());
        message.setFailureReason(reason);
        messageRepository.save(message);
    }

    private Message findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Mensagem nao encontrada"));
    }
}
