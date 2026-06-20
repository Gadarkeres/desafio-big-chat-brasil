package com.big_chat_brasil.api.domain.message;

import com.big_chat_brasil.api.domain.enums.MessagePriority;
import com.big_chat_brasil.api.domain.message.dto.QueuedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class MessageQueueService {

    private final AtomicLong sequenceGenerator = new AtomicLong();
    private final PriorityBlockingQueue<QueuedMessage> queue = new PriorityBlockingQueue<>(
            11,
            queuedMessageComparator()
    );

    public void enqueue(Message message) {
        QueuedMessage queuedMessage = new QueuedMessage(
                message.getId(),
                message.getPriority(),
                message.getQueuedAt(),
                sequenceGenerator.incrementAndGet()
        );

        queue.offer(queuedMessage);
        log.info(
                "Mensagem adicionada na fila. messageId={} priority={} sequence={} queueSize={}",
                queuedMessage.messageId(),
                queuedMessage.priority(),
                queuedMessage.sequence(),
                queue.size()
        );
    }

    public Optional<QueuedMessage> dequeue() {
        QueuedMessage queuedMessage = queue.poll();

        if (queuedMessage != null) {
            log.info(
                    "Mensagem removida da fila para processamento. messageId={} priority={} sequence={} queueSize={}",
                    queuedMessage.messageId(),
                    queuedMessage.priority(),
                    queuedMessage.sequence(),
                    queue.size()
            );
        }

        return Optional.ofNullable(queuedMessage);
    }

    private Comparator<QueuedMessage> queuedMessageComparator() {
        return Comparator
                .comparing((QueuedMessage queuedMessage) -> queuedMessage.priority() == MessagePriority.URGENT ? 0 : 1)
                .thenComparing(QueuedMessage::queuedAt)
                .thenComparingLong(QueuedMessage::sequence);
    }
}
