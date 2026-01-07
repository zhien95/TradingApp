package com.trading.account.service.impl;

import com.trading.account.dto.AccountDTO;
import com.trading.account.dto.CreateAccountRequest;
import com.trading.account.dto.UpdateBalanceRequest;
import com.trading.account.entity.Account;
import com.trading.account.repository.AccountRepository;
import com.trading.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountDTO createAccount(CreateAccountRequest request) {
        log.info("Creating account for owner: {}", request.getOwnerName());

        // 检查初始余额
        BigDecimal initialBalance = request.getInitialBalance() != null ? request.getInitialBalance() : BigDecimal.ZERO;
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        // 创建账户实体
        Account account = new Account();
        account.setOwnerName(request.getOwnerName());
        account.setBalance(initialBalance);
        account.setAvailableBalance(initialBalance);
        account.setCurrency(request.getCurrency() != null ? request.getCurrency() : "USD");

        // 生成账户号码（简单实现，实际中可能需要更复杂的生成逻辑）
        account.setAccountNumber(generateAccountNumber());

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with number: {}", savedAccount.getAccountNumber());

        return convertToDTO(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountDTO> getAccountByNumber(String accountNumber) {
        log.debug("Fetching account with number: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDTO> getAllAccounts() {
        log.debug("Fetching all accounts");
        return accountRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountDTO updateBalance(UpdateBalanceRequest request) {
        log.info("Updating balance for account: {}, amount: {}, type: {}",
                request.getAccountNumber(), request.getAmount(), request.getTransactionType());

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + request.getAccountNumber()));

        BigDecimal amount = request.getAmount() != null ? request.getAmount() : BigDecimal.ZERO;

        if ("DEBIT".equalsIgnoreCase(request.getTransactionType())) {
            return debit(account.getAccountNumber(), amount);
        } else if ("CREDIT".equalsIgnoreCase(request.getTransactionType())) {
            return credit(account.getAccountNumber(), amount);
        } else {
            throw new IllegalArgumentException("Invalid transaction type: " + request.getTransactionType() +
                    ". Use DEBIT or CREDIT");
        }
    }

    @Override
    @Transactional
    public AccountDTO debit(String accountNumber, BigDecimal amount) {
        log.info("Debiting account: {} with amount: {}", accountNumber, amount);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds in account: " + accountNumber);
        }

        account.setBalance(account.getBalance().subtract(amount));
        account.setAvailableBalance(account.getAvailableBalance().subtract(amount));

        Account updatedAccount = accountRepository.save(account);
        log.info("Successfully debited account: {}, new balance: {}", accountNumber, updatedAccount.getBalance());

        return convertToDTO(updatedAccount);
    }

    @Override
    @Transactional
    public AccountDTO credit(String accountNumber, BigDecimal amount) {
        log.info("Crediting account: {} with amount: {}", accountNumber, amount);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        account.setBalance(account.getBalance().add(amount));
        account.setAvailableBalance(account.getAvailableBalance().add(amount));

        Account updatedAccount = accountRepository.save(account);
        log.info("Successfully credited account: {}, new balance: {}", accountNumber, updatedAccount.getBalance());

        return convertToDTO(updatedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean accountExists(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).isPresent();
    }

    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setOwnerName(account.getOwnerName());
        dto.setBalance(account.getBalance());
        dto.setAvailableBalance(account.getAvailableBalance());
        dto.setCurrency(account.getCurrency());
        return dto;
    }

    private String generateAccountNumber() {
        // 简单的账户号码生成逻辑，实际实现中可能需要更复杂的逻辑以确保唯一性
        return "ACC" + System.currentTimeMillis();
    }
}