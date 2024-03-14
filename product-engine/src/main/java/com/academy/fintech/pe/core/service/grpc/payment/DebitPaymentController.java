package com.academy.fintech.pe.core.service.grpc.payment;

import com.academy.fintech.payment.DebitPayment;
import com.academy.fintech.payment.DebitPaymentServiceGrpc;
import com.academy.fintech.payment.PaymentResult;
import com.academy.fintech.pe.core.service.account.AccountOperationsService;
import com.academy.fintech.pe.core.service.account.db.Account;
import com.academy.fintech.pe.core.service.account.db.AccountService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.math.BigDecimal;

@GRpcService
@Slf4j
@RequiredArgsConstructor
public class DebitPaymentController extends DebitPaymentServiceGrpc.DebitPaymentServiceImplBase {
    private final AccountOperationsService accountOperationsService;
    private final AccountService accountService;

    @Override
    public void processPayment(DebitPayment payment, StreamObserver<PaymentResult> observer) {
        log.info("Got request: {}", payment);

        Account account = accountService.findByAgreementIdAndCode(payment.getAgreementId(), "debit");
        accountOperationsService.addToAccountBalance(account, new BigDecimal(payment.getPaymentAmount()));

        observer.onNext(PaymentResult.newBuilder().setResult(true).build());
        observer.onCompleted();
    }
}
