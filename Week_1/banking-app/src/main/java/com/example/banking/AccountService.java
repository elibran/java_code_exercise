package com.example.banking;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        if (account.getBalance() < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        return accountRepository.save(account);
    }

    public Optional<Account> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Optional<Account> updateAccountHolderName(String accountNumber, String newName) {
        Optional<Account> optional = accountRepository.findByAccountNumber(accountNumber);
        optional.ifPresent(acc -> {
            acc.setAccountHolderName(newName);
            accountRepository.save(acc);
        });
        return optional;
    }
}
