package com.big_chat_brasil.api.domain.message;

import com.big_chat_brasil.api.base.BaseEntity;
import com.big_chat_brasil.api.domain.conversation.Conversation;
import com.big_chat_brasil.api.domain.enums.MessagePriority;
import com.big_chat_brasil.api.domain.enums.MessageStatus;
import com.big_chat_brasil.api.domain.enums.SenderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MessagePriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageStatus status;

    @Column(nullable = false)
    private UUID sentById;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SenderType sentByType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(nullable = false)
    private LocalDateTime queuedAt;

    @Column
    private LocalDateTime processingAt;

    @Column
    private LocalDateTime sentAt;

    @Column
    private LocalDateTime deliveredAt;

    @Column
    private LocalDateTime readAt;

    @Column
    private LocalDateTime failedAt;

    @Column(length = 255)
    private String failureReason;
}
