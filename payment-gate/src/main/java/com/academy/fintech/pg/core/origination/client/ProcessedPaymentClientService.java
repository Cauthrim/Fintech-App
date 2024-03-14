package com.academy.fintech.pg.core.origination.client;

import com.academy.fintech.payment.ProcessedDisbursementPayment;
import com.academy.fintech.pg.core.origination.client.grpc.ProcessedPaymentGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessedPaymentClientService {
    private final ProcessedPaymentGrpcClient processedPaymentGrpcClient;

    public void confirmDisbursement(String paymentId) {
        ProcessedDisbursementPayment payment = ProcessedDisbursementPayment.newBuilder()
                .setPaymentId(paymentId).build();

        processedPaymentGrpcClient.confirmDisbursement(payment);
    }
}
