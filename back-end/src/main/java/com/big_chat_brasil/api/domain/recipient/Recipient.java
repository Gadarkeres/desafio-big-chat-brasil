package com.big_chat_brasil.api.domain.recipient;

import com.big_chat_brasil.api.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recipients")
@Getter
@Setter
public class Recipient extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 20)
    private String phone;
}
