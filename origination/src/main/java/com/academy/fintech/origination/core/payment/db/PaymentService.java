package com.academy.fintech.origination.core.payment.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment findById(String id) {
        return paymentRepository.findById(id).orElse(null);
    }
}
