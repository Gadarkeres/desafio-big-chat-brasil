package com.big_chat_brasil.api.domain.auth;

import com.big_chat_brasil.api.base.BaseEntity;
import com.big_chat_brasil.api.domain.client.Client;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "auth_tokens")
@Getter
@Setter
public class AuthToken extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    private LocalDateTime expiresAt;
}
