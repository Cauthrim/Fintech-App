package com.academy.fintech.origination.core.payment.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(schema = "origination", name = "payments")
public class Payment {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "agreement_id")
    private String agreementId;
}
