package com.academy.fintech.origination.core.scoring.client;

import com.academy.fintech.origination.core.scoring.client.grpc.ScoringGrpcClient;
import com.academy.fintech.origination.public_interface.loan.dto.LoanParameters;
import com.academy.fintech.scoring.ScoringRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoringClientService {
    private final ScoringGrpcClient scoringGrpcClient;

    public boolean scoreApplication(LoanParameters loanParameters, String salary, String clientId) {
        ScoringRequest request = ScoringRequest.newBuilder()
                .setInterest(loanParameters.interest())
                .setDisbursementAmount(loanParameters.disbursementAmount())
                .setOriginationAmount(loanParameters.originationAmount())
                .setTerm(loanParameters.term())
                .setSalary(salary)
                .setClientId(clientId)
                .build();

        return scoringGrpcClient.scoreApplication(request).getResult();
    }
}
