package com.academy.fintech.scoring.public_interface.loan.dto;

public record LoanParameters (
        String interest,
        String disbursementAmount,
        String originationAmount,
        int term
) { }
