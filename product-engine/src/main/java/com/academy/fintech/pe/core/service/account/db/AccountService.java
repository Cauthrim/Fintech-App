package com.academy.fintech.pe.core.service.account.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account findByAgreementIdAndCode(String agreementId, String code) {
        return accountRepository.findByAgreementIdAndCode(agreementId, code);
    }
}
