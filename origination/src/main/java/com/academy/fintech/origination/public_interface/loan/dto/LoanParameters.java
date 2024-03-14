package com.academy.fintech.origination.public_interface.loan.dto;

public record LoanParameters (
        String interest,
        String disbursementAmount,
        String originationAmount,
        int term
) { }
