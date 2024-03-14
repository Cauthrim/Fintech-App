package com.academy.fintech.origination.core.pg.client;

import com.academy.fintech.origination.core.pg.client.grpc.PaymentGrpcClient;
import com.academy.fintech.payment.DisbursementRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentClientService {
    private final PaymentGrpcClient paymentGrpcClient;

    public String requestDisbursement(String email, int disbursementAmount) {
        DisbursementRequest request = DisbursementRequest.newBuilder()
                .setEmail(email)
                .setDisbursementAmount(disbursementAmount).build();

        return paymentGrpcClient.requestDisbursement(request).getPaymentId();
    }
}
