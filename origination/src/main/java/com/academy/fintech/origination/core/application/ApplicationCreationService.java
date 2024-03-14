package com.academy.fintech.origination.core.application;

import com.academy.fintech.origination.configuration.AgreementMockProperty;
import com.academy.fintech.origination.core.application.db.Application;
import com.academy.fintech.origination.core.application.db.ApplicationService;
import com.academy.fintech.origination.core.application.db.ApplicationStatus;
import com.academy.fintech.origination.core.client.db.LoanClient;
import com.academy.fintech.origination.core.client.db.LoanClientService;
import com.academy.fintech.origination.core.pe.client.AgreementClientService;
import com.academy.fintech.origination.public_interface.loan.dto.AgreementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ApplicationCreationService {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    // these mocked fields' legitimate acquisition is currently out of the project's scope
    private final AgreementMockProperty mockProperties;

    private final AgreementClientService agreementClientService;
    private final ApplicationService applicationService;
    private final LoanClientService loanClientService;

    @Transactional(noRollbackFor = {AlreadyExistsException.class, IllegalArgumentException.class})
    public String create(LoanClient requestLoanClient, Application requestApplication) {
        if (!validateEmail(requestLoanClient.getEmail())) {
            throw new IllegalArgumentException();
        }

        LoanClient loanClient = loanClientService.findByEmail(requestLoanClient.getEmail())
                .orElseGet(() -> loanClientService.save(requestLoanClient));

        requestApplication.setClientId(loanClient.getId());

        checkApplicationDuplication(loanClient, requestApplication.getRequestedDisbursementAmount());

        String agreementId = agreementClientService.createAgreement(new AgreementDto(
                mockProperties.productCode(),
                loanClient.getId(),
                mockProperties.interest(),
                mockProperties.term(),
                String.valueOf(requestApplication.getRequestedDisbursementAmount()),
                mockProperties.originationAmount()
        ));
        requestApplication.setAgreementId(agreementId);

        Application application = applicationService.save(requestApplication);
        return application.getId();
    }

    @Transactional
    public boolean cancel(String applicationId) {
        Application application = applicationService.findById(applicationId);

        if (checkIsCancellable(application)) {
            applicationService.delete(application);
            return true;
        }

        return false;
    }

    private boolean validateEmail(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    private void checkApplicationDuplication(LoanClient loanClient, int requestedDisbursementAmount) {
        Application application = applicationService.findByClientIdAndStatus(loanClient.getId(), ApplicationStatus.NEW);
        if (application != null && application.getRequestedDisbursementAmount() == requestedDisbursementAmount) {
            throw new AlreadyExistsException(application.getId());
        }
    }

    private boolean checkIsCancellable(Application application) {
        return application != null && application.getStatus() == ApplicationStatus.NEW;
    }
}
