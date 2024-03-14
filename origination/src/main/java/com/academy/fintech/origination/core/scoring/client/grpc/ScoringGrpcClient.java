package com.academy.fintech.origination.core.scoring.client.grpc;

import com.academy.fintech.scoring.ScoringRequest;
import com.academy.fintech.scoring.ScoringResult;
import com.academy.fintech.scoring.ScoringServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScoringGrpcClient {
    private final ScoringServiceGrpc.ScoringServiceBlockingStub stub;

    public ScoringGrpcClient(ScoringGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ScoringServiceGrpc.newBlockingStub(channel);
    }

    public ScoringResult scoreApplication(ScoringRequest request) {
        try {
            return stub.scoreApplication(request);
        } catch (StatusRuntimeException exception) {
            log.error("Got error from Scoring by request: {}", request, exception);
            throw exception;
        }
    }
}
