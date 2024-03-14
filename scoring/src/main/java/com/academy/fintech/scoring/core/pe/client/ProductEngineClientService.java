package com.academy.fintech.scoring.core.pe.client;

import com.academy.fintech.pe.ClientId;
import com.academy.fintech.scoring.core.pe.client.grpc.ProductEngineGrpcClient;
import com.academy.fintech.scoring.public_interface.loan.dto.LoanParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductEngineClientService {
    private final ProductEngineGrpcClient productEngineGrpcClient;

    public BigDecimal getPeriodPayment(LoanParameters loanParameters) {
        return new BigDecimal(productEngineGrpcClient.getPeriodPayment(com.academy.fintech.pe.LoanParameters.newBuilder()
                .setInterest(loanParameters.interest())
                .setDisbursementAmount(loanParameters.disbursementAmount())
                .setOriginationAmount(loanParameters.originationAmount())
                .setTerm(loanParameters.term())
                .build()).getPayment());
    }

    public List<LocalDate> getOverduePayments(String clientId) {
        return productEngineGrpcClient.getOverduePaymentDates(ClientId.newBuilder().setClientId(clientId).build())
                .getDueDateList().stream().map(LocalDate::parse).toList();
    }
}
