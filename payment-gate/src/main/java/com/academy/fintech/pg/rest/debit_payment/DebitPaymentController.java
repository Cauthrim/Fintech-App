package com.academy.fintech.pg.rest.debit_payment;

import com.academy.fintech.pg.core.pe.client.DebitPaymentClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pg/payment")
public class DebitPaymentController {
    private final DebitPaymentClientService debitPaymentClientService;

    @PostMapping
    public boolean processPayment(@RequestBody DebitPaymentRequest request) {
        return debitPaymentClientService.processPayment(request.agreementId(), request.paymentAmount());
    }
}
