import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.origination.core.application.AlreadyExistsException;
import com.academy.fintech.origination.core.application.ApplicationCreationService;
import com.academy.fintech.origination.core.application.db.Application;
import com.academy.fintech.origination.core.application.db.ApplicationStatus;
import com.academy.fintech.origination.core.client.db.LoanClient;
import com.academy.fintech.origination.grpc.application.v1.ApplicationController;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class ApplicationCreationUnitConfiguration {
    @Bean
    ApplicationCreationService applicationCreationService() {
        ApplicationCreationService applicationCreationService =
                mock(ApplicationCreationService.class);

        Application requestApplication = new Application();
        requestApplication.setStatus(ApplicationStatus.NEW);
        requestApplication.setRequestedDisbursementAmount(500000);
        LoanClient requestLoanClient = new LoanClient();
        requestLoanClient.setEmail("example@mail.com");
        requestLoanClient.setSalary(100000);
        requestLoanClient.setFirstName("Danila");
        requestLoanClient.setLastName("Kurkov");
        Mockito.when(applicationCreationService.create(requestLoanClient, requestApplication)).thenReturn("ok");

        LoanClient invalidEmailLoanClient = new LoanClient();
        invalidEmailLoanClient.setEmail("example|@mail.com");
        invalidEmailLoanClient.setSalary(100000);
        invalidEmailLoanClient.setFirstName("Danila");
        invalidEmailLoanClient.setLastName("Kurkov");
        Mockito.when(applicationCreationService.create(invalidEmailLoanClient, requestApplication))
                .thenThrow(new IllegalArgumentException());


        LoanClient duplicateApplicationLoanClient = new LoanClient();
        duplicateApplicationLoanClient.setEmail("duplicate@mail.com");
        duplicateApplicationLoanClient.setSalary(100000);
        duplicateApplicationLoanClient.setFirstName("Danila");
        duplicateApplicationLoanClient.setLastName("Kurkov");
        Mockito.when(applicationCreationService.create(duplicateApplicationLoanClient, requestApplication))
                .thenThrow(new AlreadyExistsException("mock"));

        return applicationCreationService;
    }

    @Bean
    ApplicationServiceGrpc.ApplicationServiceImplBase applicationController() {
        return new ApplicationController(applicationCreationService());
    }
}
