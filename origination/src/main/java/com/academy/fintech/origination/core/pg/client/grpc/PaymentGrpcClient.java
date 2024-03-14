package com.academy.fintech.origination.core.pg.client.grpc;

import com.academy.fintech.payment.DisbursementRequest;
import com.academy.fintech.payment.DisbursementRequestResponse;
import com.academy.fintech.payment.RequestedDisbursementPaymentServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentGrpcClient {
    private final RequestedDisbursementPaymentServiceGrpc.RequestedDisbursementPaymentServiceBlockingStub stub;

    public PaymentGrpcClient(PaymentGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = RequestedDisbursementPaymentServiceGrpc.newBlockingStub(channel);
    }

    public DisbursementRequestResponse requestDisbursement(DisbursementRequest request) {
        try {
            return stub.requestDisbursement(request);
        }  catch (StatusRuntimeException exception) {
            log.error("Got error from Payment Gate by request: {}", request, exception);
            throw exception;
        }
    }
}
