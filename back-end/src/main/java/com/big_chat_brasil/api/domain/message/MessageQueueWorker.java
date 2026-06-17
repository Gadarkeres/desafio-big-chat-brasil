package com.big_chat_brasil.api.domain.message;

import com.big_chat_brasil.api.domain.enums.MessageStatus;
import com.big_chat_brasil.api.domain.message.dto.QueuedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageQueueWorker {

    private static final long DELIVERY_SIMULATION_MS = 800;

    private final MessageQueueService messageQueueService;
    private final MessageProcessingService messageProcessingService;
    private final MessageRepository messageRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void enqueuePendingMessages() {
        List<Message> pendingMessages = messageRepository.findByStatusOrderByQueuedAtAsc(MessageStatus.QUEUED);
        pendingMessages.forEach(messageQueueService::enqueue);
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
                return;
            }

            Thread.sleep(DELIVERY_SIMULATION_MS);
            messageProcessingService.markAsSent(queuedMessage.messageId());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            markAsFailedSafely(queuedMessage, "Processamento interrompido");
        } catch (Exception exception) {
            markAsFailedSafely(queuedMessage, "Falha ao processar mensagem");
        }
    }

    private void markAsFailedSafely(QueuedMessage queuedMessage, String reason) {
        messageProcessingService.markAsFailed(queuedMessage.messageId(), reason);
    }
}
