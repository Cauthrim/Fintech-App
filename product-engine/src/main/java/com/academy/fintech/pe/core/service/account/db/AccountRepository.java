package com.academy.fintech.pe.core.service.account.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    public Account findByAgreementIdAndCode(String agreementId, String code);
}
