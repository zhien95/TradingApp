package com.trading.account.controller;

import com.trading.account.dto.AccountDTO;
import com.trading.account.dto.CreateAccountRequest;
import com.trading.account.dto.UpdateBalanceRequest;
import com.trading.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody CreateAccountRequest request) {
        log.info("Received request to create account: {}", request);
        AccountDTO account = accountService.createAccount(request);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable String accountNumber) {
        log.debug("Received request to get account: {}", accountNumber);
        return accountService.getAccountByNumber(accountNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        log.debug("Received request to get all accounts");
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/balance")
    public ResponseEntity<AccountDTO> updateBalance(@RequestBody UpdateBalanceRequest request) {
        log.info("Received request to update balance: {}", request);
        AccountDTO account = accountService.updateBalance(request);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{accountNumber}/debit")
    public ResponseEntity<AccountDTO> debit(@PathVariable String accountNumber, @RequestParam("amount") java.math.BigDecimal amount) {
        log.info("Received request to debit account: {} with amount: {}", accountNumber, amount);
        AccountDTO account = accountService.debit(accountNumber, amount);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{accountNumber}/credit")
    public ResponseEntity<AccountDTO> credit(@PathVariable String accountNumber, @RequestParam("amount") java.math.BigDecimal amount) {
        log.info("Received request to credit account: {} with amount: {}", accountNumber, amount);
        AccountDTO account = accountService.credit(accountNumber, amount);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/exists/{accountNumber}")
    public ResponseEntity<Boolean> accountExists(@PathVariable String accountNumber) {
        log.debug("Received request to check if account exists: {}", accountNumber);
        boolean exists = accountService.accountExists(accountNumber);
        return ResponseEntity.ok(exists);
    }
}