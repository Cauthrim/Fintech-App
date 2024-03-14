import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.origination.core.application.ApplicationCreationService;
import com.academy.fintech.origination.grpc.application.v1.ApplicationController;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class ApplicationCancellationUnitConfiguration {
    @Bean
    ApplicationCreationService applicationCreationService() {
        ApplicationCreationService applicationCreationService = mock(ApplicationCreationService.class);

        Mockito.when(applicationCreationService.cancel("valid")).thenReturn(true);
        Mockito.when(applicationCreationService.cancel("non-existent")).thenReturn(false);

        return applicationCreationService;
    }

    @Bean
    ApplicationServiceGrpc.ApplicationServiceImplBase applicationController() {
        return new ApplicationController(applicationCreationService());
    }
}
