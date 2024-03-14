package com.academy.fintech.pe.core.service.product.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "min_term")
    private int minTerm;

    @Column(name = "max_term")
    private int maxTerm;

    @Column(name = "min_principal_amount")
    private int minPrincipalAmount;

    @Column(name = "max_principal_amount")
    private int maxPrincipalAmount;

    @Column(name = "min_interest")
    private BigDecimal minInterest;

    @Column(name = "max_interest")
    private BigDecimal maxInterest;

    @Column(name = "min_origination_amount")
    private int minOriginationAmount;

    @Column(name = "max_origination_amount")
    private int maxOriginationAmount;
}
