
package com.example.banking.service;

import com.example.banking.exception.AccountNotFoundException;
import com.example.banking.exception.FraudulentTransactionException;
import com.example.banking.exception.InsufficientFundsException;
import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final FraudDetectionService fraudDetectionService;

    public TransferService(AccountRepository accountRepository, FraudDetectionService fraudDetectionService) {
        this.accountRepository = accountRepository;
        this.fraudDetectionService = fraudDetectionService;
    }

    @Transactional
    public void transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException("From account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException("To account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Not enough balance");
        }

        boolean isFraudulent = fraudDetectionService.isTransactionFraudulent(fromAccountId, amount);
        if (isFraudulent) {
            throw new FraudulentTransactionException("Transaction flagged as fraudulent.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}
