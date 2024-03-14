package com.academy.fintech.pe.core.service.schedule.db.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentScheduleService {
    private final PaymentScheduleRepository paymentScheduleRepository;

    public PaymentSchedule save(PaymentSchedule paymentSchedule) {
        return paymentScheduleRepository.save(paymentSchedule);
    }

    public List<PaymentSchedule> findAllByAgreementId(String agreementId) {
        return paymentScheduleRepository.findAllByAgreementId(agreementId);
    }

    public PaymentSchedule findLatestVersionByAgreementId(String agreementId) {
        return paymentScheduleRepository.findAllByAgreementId(agreementId).stream()
                .max(Comparator.comparing(PaymentSchedule::getVersion)).orElse(null);
    }
}
