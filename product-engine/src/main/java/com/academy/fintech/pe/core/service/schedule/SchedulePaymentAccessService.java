package com.academy.fintech.pe.core.service.schedule;

import com.academy.fintech.pe.core.service.agreement.db.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.AgreementService;
import com.academy.fintech.pe.core.service.schedule.db.payment.SchedulePayment;
import com.academy.fintech.pe.core.service.schedule.db.payment.SchedulePaymentService;
import com.academy.fintech.pe.core.service.schedule.db.schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.schedule.db.schedule.PaymentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulePaymentAccessService {
    private final AgreementService agreementService;
    private final PaymentScheduleService paymentScheduleService;
    private final SchedulePaymentService schedulePaymentService;

    public List<String> getOverduePaymentDates(String clientId) {
        List<String> overduePaymentDates = new ArrayList<>();
        List<Agreement> clientAgreements = agreementService.findAllByClientId(clientId);
        List<PaymentSchedule> agreementSchedules = new ArrayList<>();

        for (Agreement agreement : clientAgreements) {
            agreementSchedules.addAll(paymentScheduleService.findAllByAgreementId(agreement.getId()));
        }

        for (PaymentSchedule paymentSchedule : agreementSchedules) {
            SchedulePayment earliestOverduePayment =
                    schedulePaymentService.findEarliestOverduePayment(paymentSchedule.getId());
            if (earliestOverduePayment != null) {
                overduePaymentDates.add(earliestOverduePayment.getPaymentDate().toString());
            }
        }

        return overduePaymentDates;
    }
}
