import com.academy.fintech.pe.core.service.agreement.AgreementActivationService;
import com.academy.fintech.pe.core.service.grpc.agreement.AgreementController;
import com.academy.fintech.pe.core.service.agreement.AgreementCreationService;
import com.academy.fintech.pe.core.service.agreement.db.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.AgreementRepository;
import com.academy.fintech.pe.core.service.agreement.db.AgreementService;
import com.academy.fintech.agreement.AgreementServiceGrpc;
import com.academy.fintech.pe.core.service.schedule.db.payment.SchedulePaymentService;
import com.academy.fintech.pe.core.service.schedule.db.schedule.PaymentScheduleService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;

@Configuration
public class AgreementActivationUnitConfiguration {
    @Bean
    AgreementCreationService agreementCreationService() {
        return mock(AgreementCreationService.class);
    }

    @Bean
    PaymentScheduleService paymentScheduleService() {
        return mock(PaymentScheduleService.class);
    }

    @Bean
    SchedulePaymentService schedulePaymentService() {
        return mock(SchedulePaymentService.class);
    }

    @Bean
    AgreementRepository agreementRepository() {
        Agreement agreement = new Agreement();
        agreement.setId("mock");
        agreement.setInterest(new BigDecimal(8));
        agreement.setDisbursementAmount(new BigDecimal(495000));
        agreement.setOriginationAmount(new BigDecimal(5000));
        agreement.setTerm(12);
        AgreementRepository agreementRepository = mock(AgreementRepository.class);
        Mockito.when(agreementRepository.findById("mock")).thenReturn(Optional.of(agreement));
        return agreementRepository;
    }

    @Bean
    AgreementService agreementService() {
        return new AgreementService(agreementRepository());
    }

    @Bean
    AgreementActivationService agreementActivationService() {
        return new AgreementActivationService(agreementService(), paymentScheduleService(), schedulePaymentService());
    }

    @Bean
    AgreementServiceGrpc.AgreementServiceImplBase agreementController() {
        return new AgreementController(agreementActivationService(), agreementCreationService());
    }
}
