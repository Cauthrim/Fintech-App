package com.academy.fintech.pe.core.service.schedule.db.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulePaymentService {
    private final SchedulePaymentRepository schedulePaymentRepository;

    public SchedulePayment save(SchedulePayment payment) {
        return schedulePaymentRepository.save(payment);
    }

    public void saveAll(List<SchedulePayment> payments) {
        schedulePaymentRepository.saveAll(payments);
    }

    public SchedulePayment findEarliestOverduePayment(int scheduleId) {
        return schedulePaymentRepository.findFirstByPaymentScheduleIdAndStatus(scheduleId, PaymentStatus.OVERDUE);
    }

    public SchedulePayment findByPaymentScheduleIdAndPaymentDate(int scheduleId, LocalDate date) {
        return schedulePaymentRepository.findByPaymentScheduleIdAndPaymentDate(scheduleId, date);
    }
}
