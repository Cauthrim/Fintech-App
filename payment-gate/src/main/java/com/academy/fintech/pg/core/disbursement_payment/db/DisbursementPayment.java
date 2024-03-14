package com.academy.fintech.pg.core.disbursement_payment.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(schema = "payment_gate", name = "disbursement_payments")
public class DisbursementPayment {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "email")
    private String email;

    @Column(name = "disbursement_amount")
    private int disbursementAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DisbursementPaymentStatus status;
}
