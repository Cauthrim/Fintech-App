package com.academy.fintech.scoring.core.pe.client.grpc;

import com.academy.fintech.pe.ClientId;
import com.academy.fintech.pe.LoanParameters;
import com.academy.fintech.pe.OverduePaymentDates;
import com.academy.fintech.pe.PeriodPayment;
import com.academy.fintech.pe.ProductEngineServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductEngineGrpcClient {
    private final ProductEngineServiceGrpc.ProductEngineServiceBlockingStub stub;

    public ProductEngineGrpcClient(ProductEngineGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ProductEngineServiceGrpc.newBlockingStub(channel);
    }

    public PeriodPayment getPeriodPayment(LoanParameters request) {
        try {
            return stub.getPeriodPayment(request);
        } catch (StatusRuntimeException exception) {
            log.error("Got error from Product Engine by request: {}", request, exception);
            throw exception;
        }
    }

    public OverduePaymentDates getOverduePaymentDates(ClientId request) {
        try {
            return stub.getOverduePaymentDates(request);
        } catch (StatusRuntimeException exception) {
            log.error("Got error from Product Engine by request: {}", request, exception);
            throw exception;
        }
    }
}
