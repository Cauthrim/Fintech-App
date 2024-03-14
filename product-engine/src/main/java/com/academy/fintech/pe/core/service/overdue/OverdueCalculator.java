package com.academy.fintech.pe.core.service.overdue;

import com.academy.fintech.pe.core.service.account.db.Account;
import com.academy.fintech.pe.core.service.account.db.AccountService;
import com.academy.fintech.pe.core.service.agreement.db.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.AgreementService;
import com.academy.fintech.pe.core.service.schedule.db.payment.PaymentStatus;
import com.academy.fintech.pe.core.service.schedule.db.payment.SchedulePayment;
import com.academy.fintech.pe.core.service.schedule.db.payment.SchedulePaymentService;
import com.academy.fintech.pe.core.service.schedule.db.schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.schedule.db.schedule.PaymentScheduleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OverdueCalculator {
    private final AgreementService agreementService;
    private final AccountService accountService;
    private final SchedulePaymentService schedulePaymentService;
    private final PaymentScheduleService paymentScheduleService;

    @Transactional
    @Scheduled(cron = "0 0 12 * * ?")
    public void calculateOverdue() {
        LocalDate currentDate = LocalDate.now();
        List<Agreement> duePaymentAgreements = agreementService.findAllByNextPaymentDate(currentDate);

        for (Agreement agreement : duePaymentAgreements) {
            agreement.setNextPaymentDate(agreement.getNextPaymentDate().plusMonths(1));
            agreementService.save(agreement);

            PaymentSchedule relevantSchedule = paymentScheduleService.findLatestVersionByAgreementId(agreement.getId());
            SchedulePayment duePayment = schedulePaymentService.findByPaymentScheduleIdAndPaymentDate(
                    relevantSchedule.getId(), currentDate);
            Account debitAccount = accountService.findByAgreementIdAndCode(agreement.getId(), "debit");

            if (debitAccount.getBalance().compareTo(duePayment.getPeriodPayment()) < 0) {
                duePayment.setStatus(PaymentStatus.OVERDUE);

                Account overdueAccount = accountService.findByAgreementIdAndCode(agreement.getId(), "overdue");
                overdueAccount.setBalance(overdueAccount.getBalance().add(
                        duePayment.getPeriodPayment().subtract(debitAccount.getBalance())));
                debitAccount.setBalance(new BigDecimal(0));

                schedulePaymentService.save(duePayment);
                accountService.save(debitAccount);
                accountService.save(overdueAccount);
            } else {
                duePayment.setStatus(PaymentStatus.PAID);
                debitAccount.setBalance(debitAccount.getBalance().subtract(duePayment.getPeriodPayment()));

                schedulePaymentService.save(duePayment);
                accountService.save(debitAccount);
            }
        }
    }
}
