package com.academy.fintech.origination.core.pe.client.grpc;

import com.academy.fintech.agreement.AgreementActivation;
import com.academy.fintech.agreement.AgreementServiceGrpc;
import com.academy.fintech.agreement.ProtoAgreement;
import com.academy.fintech.agreement.ProtoAgreementId;
import com.academy.fintech.agreement.ProtoPaymentSchedule;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AgreementGrpcClient {
    private final AgreementServiceGrpc.AgreementServiceBlockingStub stub;

    public AgreementGrpcClient(AgreementGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = AgreementServiceGrpc.newBlockingStub(channel);
    }

    public ProtoAgreementId createAgreement(ProtoAgreement request) {
        try {
            return stub.createAgreement(request);
        }  catch (StatusRuntimeException exception) {
            log.error("Got error from Product Engine by request: {}", request, exception);
            throw exception;
        }
    }

    public ProtoPaymentSchedule activateAgreement(AgreementActivation request) {
        try {
            return stub.activateAgreement(request);
        }  catch (StatusRuntimeException exception) {
            log.error("Got error from Product Engine by request: {}", request, exception);
            throw exception;
        }
    }
}
