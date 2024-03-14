package com.academy.fintech.pe.core.service.schedule.db.schedule;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_schedules")
public class PaymentSchedule {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "agreement_id")
    private String agreementId;

    @Column(name = "version")
    private int version;
}
