package com.academy.fintech.origination.grpc.payment;

import com.academy.fintech.origination.core.payment.db.Payment;
import com.academy.fintech.origination.core.payment.db.PaymentService;
import com.academy.fintech.origination.core.pe.client.AgreementClientService;
import com.academy.fintech.payment.Empty;
import com.academy.fintech.payment.ProcessedDisbursementPayment;
import com.academy.fintech.payment.ProcessedDisbursementPaymentServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class ProcessedPaymentController
        extends ProcessedDisbursementPaymentServiceGrpc.ProcessedDisbursementPaymentServiceImplBase {
    private final PaymentService paymentService;
    private final AgreementClientService agreementClientService;

    @Override
    public void confirmDisbursement(ProcessedDisbursementPayment processedPayment, StreamObserver<Empty> observer) {
        log.info("Got request: {}", processedPayment);

        Payment payment = paymentService.findById(processedPayment.getPaymentId());
        agreementClientService.activateAgreement(payment.getAgreementId());

        observer.onNext(Empty.newBuilder().build());
        observer.onCompleted();
    }
}
