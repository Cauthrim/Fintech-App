package com.academy.fintech.pe.core.service.agreement.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "agreements")
public class Agreement {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "interest")
    private BigDecimal interest;

    @Column(name = "term")
    private int term;

    @Column(name = "disbursement_amount")
    private BigDecimal disbursementAmount;

    @Column(name = "origination_amount")
    private BigDecimal originationAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AgreementStatus status;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @Column(name = "next_payment_date")
    private LocalDate nextPaymentDate;
}
