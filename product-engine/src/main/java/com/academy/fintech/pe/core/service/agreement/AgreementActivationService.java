package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.AgreementStatus;
import com.academy.fintech.pe.core.service.schedule.db.payment.SchedulePayment;
import com.academy.fintech.pe.core.service.schedule.db.payment.SchedulePaymentService;
import com.academy.fintech.pe.core.service.schedule.db.schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.schedule.db.schedule.PaymentScheduleService;
import com.academy.fintech.pe.core.service.util.BigDecimalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementActivationService {
    private static final int DEFAULT_VERSION = 1;

    private final AgreementService agreementService;
    private final PaymentScheduleService paymentScheduleService;
    private final SchedulePaymentService schedulePaymentService;

    @Transactional
    public PaymentSchedule activateAgreement(String agreementId, LocalDate date) {
        Agreement agreement = agreementService.findById(agreementId);

        PaymentSchedule schedule = new PaymentSchedule();
        schedule.setAgreementId(agreementId);
        schedule.setVersion(DEFAULT_VERSION);

        if (agreement == null) {
            schedule.setId(-1);
            return schedule;
        }
        schedule = paymentScheduleService.save(schedule);

        agreement.setStatus(AgreementStatus.ACTIVE);
        agreement.setDisbursementDate(date);
        agreement.setNextPaymentDate(date.plusMonths(1));
        agreementService.save(agreement);

        List<SchedulePayment> payments = BigDecimalUtils.calculatePaymentSchedule(agreement.getInterest(),
                agreement.getDisbursementAmount(), agreement.getOriginationAmount(), agreement.getTerm());
        for (SchedulePayment payment : payments) {
            payment.setPaymentScheduleId(schedule.getId());
            date = date.plusMonths(1);
            payment.setPaymentDate(date);
        }
        schedulePaymentService.saveAll(payments);

        return schedule;
    }
}
