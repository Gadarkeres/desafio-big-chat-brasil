package com.big_chat_brasil.api.domain.message;

import com.big_chat_brasil.api.domain.enums.MessageStatus;
import com.big_chat_brasil.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProcessingService {

    private final MessageRepository messageRepository;

    @Transactional
    public boolean markAsProcessing(UUID messageId) {
        Message message = findById(messageId);

        if (message.getStatus() != MessageStatus.QUEUED) {
            log.warn("Mensagem ignorada no processamento por status inesperado. messageId={} status={}", messageId, message.getStatus());
            return false;
        }

        message.setStatus(MessageStatus.PROCESSING);
        message.setProcessingAt(LocalDateTime.now());
        messageRepository.save(message);
        log.info("Mensagem marcada como em processamento. messageId={}", messageId);

        return true;
    }

    @Transactional
    public void markAsSent(UUID messageId) {
        Message message = findById(messageId);
        message.setStatus(MessageStatus.SENT);
        message.setSentAt(LocalDateTime.now());
        messageRepository.save(message);
        log.info("Mensagem marcada como enviada. messageId={}", messageId);
    }

    @Transactional
    public void markAsFailed(UUID messageId, String reason) {
        Message message = findById(messageId);
        message.setStatus(MessageStatus.FAILED);
        message.setFailedAt(LocalDateTime.now());
        message.setFailureReason(reason);
        messageRepository.save(message);
        log.error("Mensagem marcada como falha. messageId={} reason={}", messageId, reason);
    }

    private Message findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Mensagem não encontrada"));
    }
}
