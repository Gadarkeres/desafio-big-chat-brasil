package com.big_chat_brasil.api.domain.message;

import com.big_chat_brasil.api.domain.client.Client;
import com.big_chat_brasil.api.domain.conversation.Conversation;
import com.big_chat_brasil.api.domain.conversation.ConversationService;
import com.big_chat_brasil.api.domain.enums.MessagePriority;
import com.big_chat_brasil.api.domain.enums.MessageStatus;
import com.big_chat_brasil.api.domain.enums.SenderType;
import com.big_chat_brasil.api.domain.financial.FinancialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final FinancialService financialService;
    private final MessageQueueService messageQueueService;

    @Transactional(readOnly = true)
    public List<Message> findByConversation(UUID conversationId, Client client) {
        Conversation conversation = conversationService.findByIdAndClient(conversationId, client);
        return messageRepository.findByConversationIdOrderByQueuedAtAsc(conversation.getId());
    }

    @Transactional
    public Message sendMessage(Client client, SendMessageCommand command) {
        Conversation conversation = conversationService.findByIdAndClient(command.conversationId(), client);

        Message message = new Message();
        message.setConversation(conversation);
        message.setContent(command.content());
        message.setPriority(command.priority());
        message.setStatus(MessageStatus.QUEUED);
        message.setSentById(client.getId());
        message.setSentByType(SenderType.CLIENT);
        message.setCost(financialService.calculateCost(command.priority()));
        message.setQueuedAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        financialService.chargeMessage(client, savedMessage);
        messageQueueService.enqueue(savedMessage);

        return savedMessage;
    }

    public record SendMessageCommand(UUID conversationId, String content, MessagePriority priority) {
    }
}
