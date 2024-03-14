package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.CancellationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.CancellationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.origination.core.application.AlreadyExistsException;
import com.academy.fintech.origination.core.application.ApplicationCreationService;
import com.academy.fintech.origination.core.application.db.Application;
import com.academy.fintech.origination.core.client.db.LoanClient;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class ApplicationController extends ApplicationServiceGrpc.ApplicationServiceImplBase {
    private final ApplicationCreationService applicationCreationService;

    @Override
    @Transactional
    public void create(ApplicationRequest request, StreamObserver<ApplicationResponse> responseObserver) {
        log.info("Got request: {}", request);

        LoanClient loanClient = GrpcMapper.mapLoanClient(request);
        Application application = GrpcMapper.mapApplication(request);
        try {
            String applicationId = applicationCreationService.create(loanClient, application);
            responseObserver.onNext(
                    ApplicationResponse.newBuilder()
                            .setApplicationId(applicationId)
                            .build()
            );
            responseObserver.onCompleted();
        } catch (AlreadyExistsException exception) {
            Metadata trailer = new Metadata();
            Metadata.Key<String> trailerKey = Metadata.Key.of("application_id", Metadata.ASCII_STRING_MARSHALLER);
            trailer.put(trailerKey, exception.getMessage());
            responseObserver.onError(Status.ALREADY_EXISTS.asRuntimeException(trailer));
        } catch (IllegalArgumentException exception) {
            responseObserver.onError(Status.INVALID_ARGUMENT.asRuntimeException());
        }
    }

    @Override
    public void cancel(CancellationRequest request, StreamObserver<CancellationResponse> responseObserver) {
        log.info("Got request: {}", request);

        if (!applicationCreationService.cancel(request.getApplicationId())) {
            Metadata trailer = new Metadata();
            Metadata.Key<String> trailerKey = Metadata.Key.of("application_id", Metadata.ASCII_STRING_MARSHALLER);
            trailer.put(trailerKey, request.getApplicationId());
            responseObserver.onError(Status.INVALID_ARGUMENT.asRuntimeException(trailer));
            return;
        }

        responseObserver.onNext(
                CancellationResponse.newBuilder()
                        .setIsCancelled(true)
                        .build()
        );
        responseObserver.onCompleted();
    }
}
