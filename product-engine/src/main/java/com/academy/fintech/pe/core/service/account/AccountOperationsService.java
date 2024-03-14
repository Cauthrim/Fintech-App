package com.academy.fintech.pe.core.service.account;

import com.academy.fintech.pe.core.service.account.db.Account;
import com.academy.fintech.pe.core.service.account.db.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountOperationsService {
    private final AccountService accountService;

    public void addToAccountBalance(Account account, BigDecimal toBeAdded) {
        account.setBalance(account.getBalance().add(toBeAdded));

        accountService.save(account);
    }
}
