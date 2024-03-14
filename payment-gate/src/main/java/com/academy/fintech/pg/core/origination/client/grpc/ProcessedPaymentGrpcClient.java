package com.academy.fintech.pg.core.origination.client.grpc;

import com.academy.fintech.payment.Empty;
import com.academy.fintech.payment.ProcessedDisbursementPayment;
import com.academy.fintech.payment.ProcessedDisbursementPaymentServiceGrpc;
import com.academy.fintech.payment.RequestedDisbursementPaymentServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessedPaymentGrpcClient {
    private final ProcessedDisbursementPaymentServiceGrpc.ProcessedDisbursementPaymentServiceBlockingStub stub;

    public ProcessedPaymentGrpcClient(ProcessedPaymentGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ProcessedDisbursementPaymentServiceGrpc.newBlockingStub(channel);
    }

    public Empty confirmDisbursement(ProcessedDisbursementPayment payment) {
        try {
            return stub.confirmDisbursement(payment);
        }  catch (StatusRuntimeException exception) {
            log.error("Got error from Origination by request: {}", payment, exception);
            throw exception;
        }
    }
}
