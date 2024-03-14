package com.academy.fintech.pe.core.service.schedule.db.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SchedulePaymentRepository extends JpaRepository<SchedulePayment, SchedulePaymentCompositeId> {
    SchedulePayment findFirstByPaymentScheduleIdAndStatus(int scheduleId, PaymentStatus status);

    SchedulePayment findByPaymentScheduleIdAndPaymentDate(int scheduleId, LocalDate date);
}
