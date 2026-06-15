package com.big_chat_brasil.api.domain.conversation;

import com.big_chat_brasil.api.base.BaseEntity;
import com.big_chat_brasil.api.domain.client.Client;
import com.big_chat_brasil.api.domain.recipient.Recipient;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "conversations",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_conversations_client_recipient",
                columnNames = {"client_id", "recipient_id"}
        )
)
@Getter
@Setter
public class Conversation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Recipient recipient;
}
