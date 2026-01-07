package com.trading.account.service;

import com.trading.account.dto.AccountDTO;
import com.trading.account.dto.CreateAccountRequest;
import com.trading.account.dto.UpdateBalanceRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    AccountDTO createAccount(CreateAccountRequest request);

    Optional<AccountDTO> getAccountByNumber(String accountNumber);

    List<AccountDTO> getAllAccounts();

    AccountDTO updateBalance(UpdateBalanceRequest request);

    AccountDTO debit(String accountNumber, BigDecimal amount);

    AccountDTO credit(String accountNumber, BigDecimal amount);

    boolean accountExists(String accountNumber);
}