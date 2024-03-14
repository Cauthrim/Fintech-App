package com.academy.fintech.pg.core.pe.client;

import com.academy.fintech.payment.DebitPayment;
import com.academy.fintech.pg.core.pe.client.grpc.DebitPaymentGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DebitPaymentClientService {
    private final DebitPaymentGrpcClient debitPaymentGrpcClient;

    public boolean processPayment(String agreementId, String paymentAmount) {
        DebitPayment payment = DebitPayment.newBuilder()
                .setAgreementId(agreementId)
                .setPaymentAmount(paymentAmount).build();

        return debitPaymentGrpcClient.processPayment(payment).getResult();
    }
}
