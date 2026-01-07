package com.trading.account;

import com.trading.account.dto.AccountDTO;
import com.trading.account.dto.CreateAccountRequest;
import com.trading.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class AccountServiceTest {

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("trading_account_test")
            .withUsername("test")
            .withPassword("test");
    @Autowired
    private AccountService accountService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Test
    void testCreateAccount() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setOwnerName("Test User");
        request.setInitialBalance(new BigDecimal("1000.00"));
        request.setCurrency("USD");

        AccountDTO account = accountService.createAccount(request);

        assertNotNull(account.getId());
        assertEquals("Test User", account.getOwnerName());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
        assertEquals(new BigDecimal("1000.00"), account.getAvailableBalance());
        assertEquals("USD", account.getCurrency());
        assertNotNull(account.getAccountNumber());
    }

    @Test
    void testGetAccountByNumber() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setOwnerName("Test User");
        request.setInitialBalance(new BigDecimal("1000.00"));
        request.setCurrency("USD");

        AccountDTO createdAccount = accountService.createAccount(request);

        AccountDTO retrievedAccount = accountService.getAccountByNumber(createdAccount.getAccountNumber()).orElse(null);

        assertNotNull(retrievedAccount);
        assertEquals(createdAccount.getId(), retrievedAccount.getId());
        assertEquals(createdAccount.getAccountNumber(), retrievedAccount.getAccountNumber());
    }

    @Test
    void testCreditAccount() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setOwnerName("Test User");
        request.setInitialBalance(new BigDecimal("1000.00"));
        request.setCurrency("USD");

        AccountDTO account = accountService.createAccount(request);

        AccountDTO creditedAccount = accountService.credit(account.getAccountNumber(), new BigDecimal("500.00"));

        assertEquals(new BigDecimal("1500.00"), creditedAccount.getBalance());
        assertEquals(new BigDecimal("1500.00"), creditedAccount.getAvailableBalance());
    }

    @Test
    void testDebitAccount() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setOwnerName("Test User");
        request.setInitialBalance(new BigDecimal("1000.00"));
        request.setCurrency("USD");

        AccountDTO account = accountService.createAccount(request);

        AccountDTO debitedAccount = accountService.debit(account.getAccountNumber(), new BigDecimal("300.00"));

        assertEquals(new BigDecimal("700.00"), debitedAccount.getBalance());
        assertEquals(new BigDecimal("700.00"), debitedAccount.getAvailableBalance());
    }

    @Test
    void testDebitInsufficientFunds() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setOwnerName("Test User");
        request.setInitialBalance(new BigDecimal("100.00"));
        request.setCurrency("USD");

        AccountDTO account = accountService.createAccount(request);

        // 尝试提取超过余额的金额，应该抛出异常
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.debit(account.getAccountNumber(), new BigDecimal("200.00"));
        });
    }
}