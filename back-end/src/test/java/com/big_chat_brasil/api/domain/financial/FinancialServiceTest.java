package com.big_chat_brasil.api.domain.financial;

import com.big_chat_brasil.api.domain.client.Client;
import com.big_chat_brasil.api.domain.client.ClientService;
import com.big_chat_brasil.api.domain.enums.FinancialTransactionType;
import com.big_chat_brasil.api.domain.enums.MessagePriority;
import com.big_chat_brasil.api.domain.enums.PlanType;
import com.big_chat_brasil.api.domain.message.Message;
import com.big_chat_brasil.api.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FinancialServiceTest {

    @Mock
    private ClientService clientService;

    @Mock
    private FinancialTransactionRepository financialTransactionRepository;

    @InjectMocks
    private FinancialService financialService;

    @Test
    void shouldCalculateMessageCostByPriority() {
        assertEquals(new BigDecimal("0.25"), financialService.calculateCost(MessagePriority.NORMAL));
        assertEquals(new BigDecimal("0.50"), financialService.calculateCost(MessagePriority.URGENT));
    }

    @Test
    void shouldDebitPrepaidBalanceAndRegisterTransaction() {
        Client client = client(PlanType.PREPAID);
        client.setBalance(new BigDecimal("10.00"));
        Message message = message(new BigDecimal("0.50"));

        financialService.chargeMessage(client, message);

        assertEquals(new BigDecimal("9.50"), client.getBalance());
        verify(clientService).save(client);

        ArgumentCaptor<FinancialTransaction> transactionCaptor = ArgumentCaptor.forClass(FinancialTransaction.class);
        verify(financialTransactionRepository).save(transactionCaptor.capture());

        FinancialTransaction transaction = transactionCaptor.getValue();
        assertEquals(client, transaction.getClient());
        assertEquals(message, transaction.getMessage());
        assertEquals(FinancialTransactionType.DEBIT, transaction.getType());
        assertEquals(new BigDecimal("0.50"), transaction.getAmount());
    }

    @Test
    void shouldRejectPrepaidMessageWhenBalanceIsInsufficient() {
        Client client = client(PlanType.PREPAID);
        client.setBalance(new BigDecimal("0.25"));
        Message message = message(new BigDecimal("0.50"));

        assertThrows(BadRequestException.class, () -> financialService.chargeMessage(client, message));

        verify(clientService, never()).save(any());
        verify(financialTransactionRepository, never()).save(any());
    }

    @Test
    void shouldConsumePostpaidLimitAndRegisterTransaction() {
        Client client = client(PlanType.POSTPAID);
        client.setMonthlyLimit(new BigDecimal("50.00"));
        client.setMonthlyUsed(new BigDecimal("40.00"));
        Message message = message(new BigDecimal("0.50"));

        financialService.chargeMessage(client, message);

        assertEquals(new BigDecimal("40.50"), client.getMonthlyUsed());
        verify(clientService).save(client);

        ArgumentCaptor<FinancialTransaction> transactionCaptor = ArgumentCaptor.forClass(FinancialTransaction.class);
        verify(financialTransactionRepository).save(transactionCaptor.capture());

        FinancialTransaction transaction = transactionCaptor.getValue();
        assertEquals(FinancialTransactionType.LIMIT_USAGE, transaction.getType());
        assertEquals(new BigDecimal("0.50"), transaction.getAmount());
    }

    @Test
    void shouldRejectPostpaidMessageWhenMonthlyLimitIsInsufficient() {
        Client client = client(PlanType.POSTPAID);
        client.setMonthlyLimit(new BigDecimal("50.00"));
        client.setMonthlyUsed(new BigDecimal("49.75"));
        Message message = message(new BigDecimal("0.50"));

        assertThrows(BadRequestException.class, () -> financialService.chargeMessage(client, message));

        verify(clientService, never()).save(any());
        verify(financialTransactionRepository, never()).save(any());
    }

    private Client client(PlanType planType) {
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setPlanType(planType);
        return client;
    }

    private Message message(BigDecimal cost) {
        Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setCost(cost);
        return message;
    }
}
