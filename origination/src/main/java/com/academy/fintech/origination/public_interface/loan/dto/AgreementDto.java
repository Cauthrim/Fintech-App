package com.academy.fintech.origination.public_interface.loan.dto;

public record AgreementDto (
    String productCode,
    String clientId,
    String interest,
    int term,
    String disbursementAmount,
    String originationAmount
) {}
