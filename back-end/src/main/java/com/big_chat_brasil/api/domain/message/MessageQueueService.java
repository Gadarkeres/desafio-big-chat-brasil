package com.big_chat_brasil.api.domain.message;

import com.big_chat_brasil.api.domain.enums.MessagePriority;
import com.big_chat_brasil.api.domain.message.dto.QueuedMessage;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MessageQueueService {

    private final AtomicLong sequenceGenerator = new AtomicLong();
    private final PriorityBlockingQueue<QueuedMessage> queue = new PriorityBlockingQueue<>(
            11,
            queuedMessageComparator()
    );

    public void enqueue(Message message) {
        queue.offer(new QueuedMessage(
                message.getId(),
                message.getPriority(),
                message.getQueuedAt(),
                sequenceGenerator.incrementAndGet()
        ));
    }

    public Optional<QueuedMessage> dequeue() {
        return Optional.ofNullable(queue.poll());
    }

    private Comparator<QueuedMessage> queuedMessageComparator() {
        return Comparator
                .comparing((QueuedMessage queuedMessage) -> queuedMessage.priority() == MessagePriority.URGENT ? 0 : 1)
                .thenComparing(QueuedMessage::queuedAt)
                .thenComparingLong(QueuedMessage::sequence);
    }
}
