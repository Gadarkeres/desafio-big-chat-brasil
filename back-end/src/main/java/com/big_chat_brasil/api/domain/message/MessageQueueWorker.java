package com.big_chat_brasil.api.domain.message;

import com.big_chat_brasil.api.domain.enums.MessageStatus;
import com.big_chat_brasil.api.domain.message.dto.QueuedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageQueueWorker {

    private static final long DELIVERY_SIMULATION_MS = 800;

    private final MessageQueueService messageQueueService;
    private final MessageProcessingService messageProcessingService;
    private final MessageRepository messageRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void enqueuePendingMessages() {
        List<Message> pendingMessages = messageRepository.findByStatusOrderByQueuedAtAsc(MessageStatus.QUEUED);
        pendingMessages.forEach(messageQueueService::enqueue);
        log.info("Mensagens pendentes reenfileiradas ao iniciar aplicação. total={}", pendingMessages.size());
    }

    @Scheduled(fixedDelay = 2_000)
    public void processNextMessage() {
        messageQueueService.dequeue()
                .ifPresent(this::process);
    }

    private void process(QueuedMessage queuedMessage) {
        try {
            boolean shouldProcess = messageProcessingService.markAsProcessing(queuedMessage.messageId());

            if (!shouldProcess) {
                log.warn("Mensagem removida da fila não será processada. messageId={}", queuedMessage.messageId());
                return;
            }

            Thread.sleep(DELIVERY_SIMULATION_MS);
            messageProcessingService.markAsSent(queuedMessage.messageId());
        } catch (InterruptedException exception) { // Thread interrompida
            Thread.currentThread().interrupt();
            log.error("Worker interrompido durante processamento. messageId={}", queuedMessage.messageId(), exception);
            markAsFailedSafely(queuedMessage, "Processamento interrompido");
        } catch (Exception exception) {
            log.error("Erro inesperado no worker de mensagens. messageId={}", queuedMessage.messageId(), exception);
            markAsFailedSafely(queuedMessage, "Falha ao processar mensagem");
        }
    }

    private void markAsFailedSafely(QueuedMessage queuedMessage, String reason) {
        messageProcessingService.markAsFailed(queuedMessage.messageId(), reason);
    }
}
