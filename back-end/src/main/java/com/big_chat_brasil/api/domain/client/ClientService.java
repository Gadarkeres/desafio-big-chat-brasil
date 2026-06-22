package com.big_chat_brasil.api.domain.client;

import com.big_chat_brasil.api.domain.enums.DocumentType;
import com.big_chat_brasil.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Client findByDocument(String documentId, DocumentType documentType) {
        return clientRepository.findByDocumentIdAndDocumentType(documentId, documentType)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
    }

    @Transactional
    public Client save(Client client) {
        return clientRepository.save(client);
    }
}
