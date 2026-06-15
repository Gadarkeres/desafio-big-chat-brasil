package com.big_chat_brasil.api.domain.client;

import com.big_chat_brasil.api.base.BaseEntity;
import com.big_chat_brasil.api.domain.enums.DocumentType;
import com.big_chat_brasil.api.domain.enums.PlanType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String documentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 4)
    private DocumentType documentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PlanType planType;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(precision = 10, scale = 2)
    private BigDecimal monthlyLimit;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyUsed = BigDecimal.ZERO;
}
