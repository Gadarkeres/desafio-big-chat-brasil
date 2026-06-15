package com.big_chat_brasil.api.domain.client;

import com.big_chat_brasil.api.domain.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByDocumentIdAndDocumentType(String documentId, DocumentType documentType);
}
