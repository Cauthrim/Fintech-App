package com.academy.fintech.pe.core.service.schedule.db.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@IdClass(SchedulePaymentCompositeId.class)
@Table(name = "schedule_payments")
public class SchedulePayment {
    @Id
    @Column(name = "payment_schedule_id")
    int paymentScheduleId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    @Column(name = "payment_date")
    LocalDate paymentDate;

    @Column(name = "period_payment")
    BigDecimal periodPayment;

    @Column(name = "interest_payment")
    BigDecimal interestPayment;

    @Column(name = "principal_payment")
    BigDecimal principalPayment;

    @Id
    @Column(name = "period_number")
    int periodNumber;
}
