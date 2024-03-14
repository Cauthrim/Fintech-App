package com.academy.fintech.pg.core.disbursement_payment.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisbursementPaymentRepository extends JpaRepository<DisbursementPayment, String> {
    List<DisbursementPayment> findAllByStatus(DisbursementPaymentStatus status);
}
