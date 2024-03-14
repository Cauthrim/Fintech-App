import com.academy.fintech.scoring.core.ScoringService;
import com.academy.fintech.scoring.public_interface.loan.dto.LoanParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@SpringJUnitConfig(classes = { ScoringUnitConfiguration.class })
@DisplayName("Scoring Tests")
public class ScoringUnitTest {
    @Autowired
    ScoringService scoringService;

    @Test
    @DisplayName("Accepted Application")
    void testAcceptedApplication() {
        LoanParameters loanParameters = new LoanParameters("8", "400000", "10000", 12);

        assertTrue(scoringService.scoreApplication(loanParameters, new BigDecimal(130000), "noOverdue@m.com"));
    }

    @Test
    @DisplayName("Good Salary Bad Overdue")
    void testGoodSalaryBadOverdue() {
        LoanParameters loanParameters = new LoanParameters("8", "400000", "10000", 12);

        assertFalse(scoringService.scoreApplication(loanParameters, new BigDecimal(130000), "seriousOverdue@m.com"));
    }

    @Test
    @DisplayName("Bad Salary No overdue")
    void testBadSalaryNoOverdue() {
        LoanParameters loanParameters = new LoanParameters("8", "400000", "10000", 12);

        assertTrue(scoringService.scoreApplication(loanParameters, new BigDecimal(10000), "noOverdue@m.com"));
    }

    @Test
    @DisplayName("Bad Salary Mild Overdue")
    void testBadSalaryMildOverdue() {
        LoanParameters loanParameters = new LoanParameters("8", "400000", "10000", 12);

        assertFalse(scoringService.scoreApplication(loanParameters, new BigDecimal(20000), "mildOverdue@m.com"));
    }
}
