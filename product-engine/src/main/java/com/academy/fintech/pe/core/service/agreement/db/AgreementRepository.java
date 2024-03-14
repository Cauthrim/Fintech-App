package com.academy.fintech.pe.core.service.agreement.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, String> {
    List<Agreement> findAllByClientId(String clientId);

    List<Agreement> findAllByNextPaymentDate(LocalDate date);
}
