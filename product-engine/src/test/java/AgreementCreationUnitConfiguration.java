import com.academy.fintech.agreement.AgreementServiceGrpc;
import com.academy.fintech.pe.core.service.account.db.AccountService;
import com.academy.fintech.pe.core.service.agreement.AgreementActivationService;
import com.academy.fintech.pe.core.service.grpc.agreement.AgreementController;
import com.academy.fintech.pe.core.service.agreement.AgreementCreationService;
import com.academy.fintech.pe.core.service.agreement.db.AgreementRepository;
import com.academy.fintech.pe.core.service.agreement.db.AgreementService;
import com.academy.fintech.pe.core.service.product.db.Product;
import com.academy.fintech.pe.core.service.product.db.ProductRepository;
import com.academy.fintech.pe.core.service.product.db.ProductService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;

@Configuration
public class AgreementCreationUnitConfiguration {
    @Bean
    AgreementService agreementService() {
        return mock(AgreementService.class);
    }

    @Bean
    AgreementActivationService agreementActivationService() {
        return mock(AgreementActivationService.class);
    }

    @Bean
    ProductRepository productRepository() {
        Product cl = new Product();
        cl.setCode("Cl1.0");
        cl.setMinTerm(3);
        cl.setMaxTerm(24);
        cl.setMinInterest(new BigDecimal(8));
        cl.setMaxInterest(new BigDecimal(15));
        cl.setMinOriginationAmount(1000);
        cl.setMaxOriginationAmount(10000);
        cl.setMinPrincipalAmount(50000);
        cl.setMaxPrincipalAmount(500000);
        ProductRepository productRepository = mock(ProductRepository.class);
        Mockito.when(productRepository.findByCode("Cl1.0")).thenReturn(cl);
        return productRepository;
    }

    @Bean
    ProductService productService() {
        return new ProductService(productRepository());
    }

    @Bean
    AgreementRepository agreementRepository() {
        return mock(AgreementRepository.class);
    }

    @Bean
    AccountService accountService() {
        return mock(AccountService.class);
    }

    @Bean
    AgreementCreationService agreementCreationService() {
        return new AgreementCreationService(agreementService(), productService(), accountService());
    }

    @Bean
    AgreementServiceGrpc.AgreementServiceImplBase agreementController() {
        return new AgreementController(agreementActivationService(), agreementCreationService());
    }
}
