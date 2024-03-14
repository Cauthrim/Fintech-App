package com.academy.fintech.pe.core.service.agreement.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgreementService {
    private final AgreementRepository agreementRepository;

    public Agreement findById(String agreementId) {
        return agreementRepository.findById(agreementId).orElse(null);
    }

    public List<Agreement> findAllByClientId(String clientId) {
        return agreementRepository.findAllByClientId(clientId);
    }

    public List<Agreement> findAllByNextPaymentDate(LocalDate date) {
        return agreementRepository.findAllByNextPaymentDate(date);
    }

    public Agreement save(Agreement agreement) {
        return agreementRepository.save(agreement);
    }
}
