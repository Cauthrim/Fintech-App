package com.academy.fintech.pg.core.pe.client.grpc;

import com.academy.fintech.payment.DebitPayment;
import com.academy.fintech.payment.DebitPaymentServiceGrpc;
import com.academy.fintech.payment.PaymentResult;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DebitPaymentGrpcClient {
    private final DebitPaymentServiceGrpc.DebitPaymentServiceBlockingStub stub;

    public DebitPaymentGrpcClient(DebitPaymentGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = DebitPaymentServiceGrpc.newBlockingStub(channel);
    }

    public PaymentResult processPayment(DebitPayment request) {
        try {
            return stub.processPayment(request);
        }  catch (StatusRuntimeException exception) {
            log.error("Got error from Product Engine by request: {}", request, exception);
            throw exception;
        }
    }
}
