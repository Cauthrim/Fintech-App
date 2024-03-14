package com.academy.fintech.pe.core.service.util;

import com.academy.fintech.pe.core.service.schedule.db.payment.PaymentStatus;
import com.academy.fintech.pe.core.service.schedule.db.payment.SchedulePayment;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BigDecimalUtils {
    private static final int MATH_SCALE = 10;
    private static final int SHOW_SCALE = 2;
    private static final BigDecimal ONE = BigDecimal.ONE.setScale(MATH_SCALE, RoundingMode.HALF_EVEN);
    private static final BigDecimal PERIOD_COUNT = new BigDecimal(12).setScale(MATH_SCALE, RoundingMode.HALF_EVEN);

    public static List<SchedulePayment> calculatePaymentSchedule(BigDecimal interest, BigDecimal disbursementAmount,
                                                                 BigDecimal originationAmount, int term) {
        BigDecimal periodicInterest = interest.setScale(MATH_SCALE, RoundingMode.HALF_EVEN)
                .divide(new BigDecimal(100), RoundingMode.HALF_EVEN)
                .divide(PERIOD_COUNT, RoundingMode.HALF_EVEN);
        BigDecimal principal = disbursementAmount.add(originationAmount).setScale(SHOW_SCALE, RoundingMode.HALF_EVEN);
        return calculatePayments(principal, periodicInterest, term);
    }

    private static List<SchedulePayment> calculatePayments(BigDecimal principal,
                                                           BigDecimal periodicInterest, int term) {
        List<SchedulePayment> payments = new ArrayList<>();

        BigDecimal balance = principal;
        BigDecimal PMT = calculatePMT(balance, periodicInterest, term);
        BigDecimal IPMT;
        BigDecimal PPMT;
        for (int period = 0; period < term; period++) {
            if (period == term - 1) {
                IPMT = PMT.subtract(balance);
                PPMT = balance;
                balance = BigDecimal.ZERO;
            } else {
                IPMT = calculateIPMT(PMT, principal, periodicInterest, period);
                PPMT = calculatePPMT(IPMT, periodicInterest, period, term);
                balance = balance.subtract(PPMT);
            }

            SchedulePayment schedulePayment = new SchedulePayment();
            schedulePayment.setStatus(PaymentStatus.FUTURE);
            schedulePayment.setPeriodPayment(PMT);
            schedulePayment.setInterestPayment(IPMT);
            schedulePayment.setPrincipalPayment(PPMT);
            schedulePayment.setPeriodNumber(period + 1);
            payments.add(schedulePayment);
        }

        return payments;
    }

    private static BigDecimal calculatePMT(BigDecimal principal, BigDecimal periodicInterest, int loanTerm) {
        return principal.multiply(periodicInterest)
                .divide(ONE.subtract(ONE.divide((periodicInterest.add(ONE)).pow(loanTerm), RoundingMode.HALF_EVEN)),
                        RoundingMode.HALF_EVEN)
                .setScale(SHOW_SCALE, RoundingMode.HALF_EVEN);
    }

    private static BigDecimal calculateIPMT(BigDecimal PMT, BigDecimal principal, BigDecimal periodicInterest,
                                            int period) {
        return PMT.add(ONE.add(periodicInterest)
                        .pow(period)
                        .multiply(principal.multiply(periodicInterest).subtract(PMT)))
                .setScale(SHOW_SCALE, RoundingMode.HALF_EVEN);
    }

    private static BigDecimal calculatePPMT(BigDecimal IPMT, BigDecimal periodicInterest, int period, int loanTerm) {
        return IPMT.divide(ONE.add(periodicInterest).pow(loanTerm - period).subtract(ONE), RoundingMode.HALF_EVEN)
                .setScale(SHOW_SCALE, RoundingMode.HALF_EVEN);
    }
}
