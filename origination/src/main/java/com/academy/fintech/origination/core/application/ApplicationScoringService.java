package com.academy.fintech.origination.core.application;

import com.academy.fintech.origination.configuration.AgreementMockProperty;
import com.academy.fintech.origination.core.application.db.Application;
import com.academy.fintech.origination.core.application.db.ApplicationService;
import com.academy.fintech.origination.core.application.db.ApplicationStatus;
import com.academy.fintech.origination.core.client.db.LoanClient;
import com.academy.fintech.origination.core.client.db.LoanClientService;
import com.academy.fintech.origination.core.payment.db.Payment;
import com.academy.fintech.origination.core.payment.db.PaymentService;
import com.academy.fintech.origination.core.pg.client.PaymentClientService;
import com.academy.fintech.origination.core.scoring.client.ScoringClientService;
import com.academy.fintech.origination.public_interface.email.EmailService;
import com.academy.fintech.origination.public_interface.loan.dto.LoanParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationScoringService {
    // these mock fields' legitimate acquisition is currently out of the project's scope
    private final AgreementMockProperty mockProperties;

    private static final String ACCEPTED_MESSAGE = "Congratulations! Your loan application has been accepted.";
    private static final String REJECTED_MESSAGE = "Unfortunately, your loan application has been rejected.";
    private static final String SUBJECT = "Loan application";

    private final EmailService emailService;
    private final ApplicationService applicationService;
    private final LoanClientService loanClientService;
    private final ScoringClientService scoringClientService;
    private final PaymentClientService paymentClientService;
    private final PaymentService paymentService;

    @Transactional
    @Scheduled(fixedDelay = 100)
    public void scoreNewApplications() {
        List<Application> newApplications = applicationService.findAllNewApplications();

        for (Application application : newApplications) {
            String clientId = application.getClientId();
            LoanClient loanClient = loanClientService.findById(clientId);
            if (loanClient == null) {
                continue;
            }

            application.setStatus(ApplicationStatus.SCORING);
            applicationService.save(application);

            LoanParameters loanParameters = new LoanParameters(
                    mockProperties.interest(),
                    String.valueOf(application.getRequestedDisbursementAmount()),
                    mockProperties.originationAmount(),
                    mockProperties.term());

            boolean scoringResult = scoringClientService.scoreApplication(
                    loanParameters,
                    String.valueOf(loanClient.getSalary()),
                    clientId);

            if (scoringResult) {
                emailService.sendEmail(ACCEPTED_MESSAGE, SUBJECT, loanClient.getEmail());
                application.setStatus(ApplicationStatus.ACCEPTED);

                String requestedPaymentId = paymentClientService.requestDisbursement(loanClient.getEmail(),
                        application.getRequestedDisbursementAmount());
                Payment requestedPayment = new Payment();
                requestedPayment.setId(requestedPaymentId);
                requestedPayment.setAgreementId(application.getAgreementId());
                paymentService.save(requestedPayment);
            } else {
                emailService.sendEmail(REJECTED_MESSAGE, SUBJECT, loanClient.getEmail());
                application.setStatus(ApplicationStatus.CLOSED);
            }
            applicationService.save(application);
        }
    }
}
