package com.academy.fintech.pe.core.service.account.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "agreement_id")
    private String agreementId;

    @Column(name = "code")
    private String code;

    @Column(name = "balance")
    private BigDecimal balance;

    public void setNonIdFields(String agreementId, String code, BigDecimal balance) {
        this.agreementId = agreementId;
        this.code = code;
        this.balance = balance;
    }
}
