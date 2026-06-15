package com.big_chat_brasil.api.domain.financial;

import com.big_chat_brasil.api.domain.client.Client;
import com.big_chat_brasil.api.domain.client.ClientService;
import com.big_chat_brasil.api.domain.enums.FinancialTransactionType;
import com.big_chat_brasil.api.domain.enums.MessagePriority;
import com.big_chat_brasil.api.domain.enums.PlanType;
import com.big_chat_brasil.api.domain.message.Message;
import com.big_chat_brasil.api.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FinancialService {

    private static final BigDecimal NORMAL_MESSAGE_COST = new BigDecimal("0.25");
    private static final BigDecimal URGENT_MESSAGE_COST = new BigDecimal("0.50");

    private final ClientService clientService;
    private final FinancialTransactionRepository financialTransactionRepository;

    public BigDecimal calculateCost(MessagePriority priority) {
        return switch (priority) {
            case NORMAL -> NORMAL_MESSAGE_COST;
            case URGENT -> URGENT_MESSAGE_COST;
        };
    }

    @Transactional
    public void chargeMessage(Client client, Message message) {
        BigDecimal cost = message.getCost();

        if (client.getPlanType() == PlanType.PREPAID) {
            chargePrepaid(client, cost);
            registerTransaction(client, message, FinancialTransactionType.DEBIT, cost, "Debito de mensagem");
            return;
        }

        chargePostpaid(client, cost);
        registerTransaction(client, message, FinancialTransactionType.LIMIT_USAGE, cost, "Consumo de limite mensal");
    }

    private void chargePrepaid(Client client, BigDecimal cost) {
        BigDecimal balance = client.getBalance() == null ? BigDecimal.ZERO : client.getBalance();

        if (balance.compareTo(cost) < 0) {
            throw new BadRequestException("Saldo insuficiente para enviar mensagem");
        }

        client.setBalance(balance.subtract(cost));
        clientService.save(client);
    }

    private void chargePostpaid(Client client, BigDecimal cost) {
        BigDecimal monthlyLimit = client.getMonthlyLimit() == null ? BigDecimal.ZERO : client.getMonthlyLimit();
        BigDecimal monthlyUsed = client.getMonthlyUsed() == null ? BigDecimal.ZERO : client.getMonthlyUsed();
        BigDecimal newMonthlyUsed = monthlyUsed.add(cost);

        if (newMonthlyUsed.compareTo(monthlyLimit) > 0) {
            throw new BadRequestException("Limite mensal insuficiente para enviar mensagem");
        }

        client.setMonthlyUsed(newMonthlyUsed);
        clientService.save(client);
    }

    private void registerTransaction(
            Client client,
            Message message,
            FinancialTransactionType type,
            BigDecimal amount,
            String description
    ) {
        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setClient(client);
        transaction.setMessage(message);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setDescription(description);

        financialTransactionRepository.save(transaction);
    }
}
