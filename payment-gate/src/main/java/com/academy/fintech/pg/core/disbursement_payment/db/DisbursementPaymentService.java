package com.academy.fintech.pg.core.disbursement_payment.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisbursementPaymentService {
    private final DisbursementPaymentRepository disbursementPaymentRepository;

    public List<DisbursementPayment> findAllByStatus(DisbursementPaymentStatus status) {
        return disbursementPaymentRepository.findAllByStatus(status);
    }

    public DisbursementPayment save(DisbursementPayment payment) {
        return disbursementPaymentRepository.save(payment);
    }
}
