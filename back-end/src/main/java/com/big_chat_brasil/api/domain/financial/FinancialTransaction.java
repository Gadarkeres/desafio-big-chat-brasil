package com.big_chat_brasil.api.domain.financial;

import com.big_chat_brasil.api.base.BaseEntity;
import com.big_chat_brasil.api.domain.client.Client;
import com.big_chat_brasil.api.domain.enums.FinancialTransactionType;
import com.big_chat_brasil.api.domain.message.Message;
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

@Entity
@Table(name = "financial_transactions")
@Getter
@Setter
public class FinancialTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FinancialTransactionType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 255)
    private String description;
}
