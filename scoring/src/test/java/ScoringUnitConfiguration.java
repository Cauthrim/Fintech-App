import com.academy.fintech.scoring.configuration.ScoringProperties;
import com.academy.fintech.scoring.core.ScoringService;
import com.academy.fintech.scoring.core.pe.client.ProductEngineClientService;
import com.academy.fintech.scoring.public_interface.loan.dto.LoanParameters;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

@Configuration
public class ScoringUnitConfiguration {
    @Bean
    ProductEngineClientService productEngineClientService() {
        ProductEngineClientService productEngineClientService = mock(ProductEngineClientService.class);

        LoanParameters loanParameters = new LoanParameters("8", "400000", "10000", 12);
        Mockito.when(productEngineClientService.getPeriodPayment(loanParameters))
                .thenReturn(new BigDecimal(40000));

        Mockito.when(productEngineClientService.getOverduePayments("noOverdue@m.com")).thenReturn(new ArrayList<>());

        List<LocalDate> mildOverdue = new ArrayList<>();
        mildOverdue.add(LocalDate.now().minusDays(6));
        Mockito.when(productEngineClientService.getOverduePayments("mildOverdue@m.com")).thenReturn(mildOverdue);

        List<LocalDate> seriousOverdue = new ArrayList<>();
        seriousOverdue.add(LocalDate.now().minusDays(10));
        Mockito.when(productEngineClientService.getOverduePayments("seriousOverdue@m.com")).thenReturn(seriousOverdue);

        return productEngineClientService;
    }

    @Bean
    ScoringService scoringService() {
        return new ScoringService(new ScoringProperties(7, 3), productEngineClientService());
    }
}
