package com.academy.fintech.scoring.core;

import com.academy.fintech.scoring.configuration.ScoringProperties;
import com.academy.fintech.scoring.core.pe.client.ProductEngineClientService;
import com.academy.fintech.scoring.public_interface.loan.dto.LoanParameters;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ScoringService {
    private final ScoringProperties scoringProperties;
    private final ProductEngineClientService productEngineClientService;

    public ScoringService(ScoringProperties scoringProperties, ProductEngineClientService productEngineClientService) {
        this.scoringProperties = scoringProperties;
        this.productEngineClientService = productEngineClientService;
    }

    public boolean scoreApplication(LoanParameters loanParameters, BigDecimal salary, String clientId) {
        int score = 0;

        score += salaryScoring(loanParameters, salary);

        score += overduePaymentsScoring(clientId);

        return score > 0;
    }

    /**
     * Checks if the client's salary is high enough compared to period payment.
     *
     * @param loanParameters - parameters needed to get period payment.
     * @param salary - the salary to check.
     * @return the result of the check, 1 if salary is enough, 0 otherwise.
     */
    private int salaryScoring(LoanParameters loanParameters, BigDecimal salary) {
        BigDecimal periodPayment = productEngineClientService.getPeriodPayment(loanParameters);

        if (salary.compareTo(periodPayment.multiply(new BigDecimal(scoringProperties.salaryThreshold()))) >= 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private int overduePaymentsScoring(String clientId) {
        // score for no overdue payments is increased by 1 by default
        int result = 1;

        LocalDate currentDate = LocalDate.now();
        List<LocalDate> overduePaymentDates = productEngineClientService.getOverduePayments(clientId);
        if (overduePaymentDates != null) {
            // if there is an overdue payment, the score is reduced by 1
            if (!overduePaymentDates.isEmpty()) {
                result -= 1;
            }

            result += checkPaymentsForSignificantOverdue(overduePaymentDates, currentDate);
        }

        return result;
    }

    /**
     * Checks the overdue payments of the client for significant overdue.
     *
     * @param overduePaymentDates - dates to check for significant overdue.
     * @return the result of the check,
     * -1 if there are significantly overdue payments, 0 otherwise.
     */
    private int checkPaymentsForSignificantOverdue(List<LocalDate> overduePaymentDates, LocalDate currentDate) {
        for (LocalDate date : overduePaymentDates) {
            if (date.plusDays(scoringProperties.significantOverduePeriod()).isBefore(currentDate)) {
                return -1;
            }
        }

        return 0;
    }
}
