package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.origination.core.application.db.Application;
import com.academy.fintech.origination.core.application.db.ApplicationStatus;
import com.academy.fintech.origination.core.client.db.LoanClient;
import org.springframework.stereotype.Component;

@Component
public class GrpcMapper {
    public static LoanClient mapLoanClient(ApplicationRequest request) {
        LoanClient loanClient = new LoanClient();
        loanClient.setFirstName(request.getFirstName());
        loanClient.setLastName(request.getLastName());
        loanClient.setEmail(request.getEmail());
        loanClient.setSalary(request.getSalary());
        return loanClient;
    }

    public static Application mapApplication(ApplicationRequest request) {
        Application application = new Application();
        application.setRequestedDisbursementAmount(request.getDisbursementAmount());
        application.setStatus(ApplicationStatus.NEW);
        return application;
    }
}