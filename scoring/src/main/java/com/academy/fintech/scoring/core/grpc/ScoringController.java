package com.academy.fintech.scoring.core.grpc;

import com.academy.fintech.scoring.ScoringRequest;
import com.academy.fintech.scoring.ScoringResult;
import com.academy.fintech.scoring.ScoringServiceGrpc;
import com.academy.fintech.scoring.core.ScoringService;
import com.academy.fintech.scoring.public_interface.loan.dto.LoanParameters;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.math.BigDecimal;

@GRpcService
@Slf4j
@RequiredArgsConstructor
public class ScoringController extends ScoringServiceGrpc.ScoringServiceImplBase {
    private final ScoringService scoringService;

    @Override
    public void scoreApplication(ScoringRequest request, StreamObserver<ScoringResult> observer) {
        log.info("Got request: {}", request);

        LoanParameters loanParameters = new LoanParameters(
                request.getInterest(),
                request.getDisbursementAmount(),
                request.getOriginationAmount(),
                request.getTerm()
        );
        boolean scoringResult = scoringService.scoreApplication(loanParameters,
                new BigDecimal(request.getSalary()), request.getClientId());
        observer.onNext(ScoringResult.newBuilder().setResult(scoringResult).build());
        observer.onCompleted();
    }
}
