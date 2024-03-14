package com.academy.fintech.pg.rest.debit_payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DebitPaymentRequest(
    @JsonProperty("agreement_id")
    String agreementId,
    String paymentAmount
) {}
