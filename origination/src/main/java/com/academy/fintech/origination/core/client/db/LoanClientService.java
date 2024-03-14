package com.academy.fintech.origination.core.client.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanClientService {
    private final LoanClientRepository loanClientRepository;

    public Optional<LoanClient> findByEmail(String email) {
        return loanClientRepository.findByEmail(email);
    }

    public LoanClient findById(String clientId) {
        return loanClientRepository.findById(clientId).orElse(null);
    }

    public LoanClient save(LoanClient loanClient) {
        return loanClientRepository.save(loanClient);
    }
}
