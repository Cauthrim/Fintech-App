package com.academy.fintech.pg.grpc.disbursement_payment;

import com.academy.fintech.payment.DisbursementRequest;
import com.academy.fintech.payment.DisbursementRequestResponse;
import com.academy.fintech.payment.RequestedDisbursementPaymentServiceGrpc;
import com.academy.fintech.pg.core.disbursement_payment.DisbursementPaymentMerchantService;
import com.academy.fintech.pg.core.disbursement_payment.db.DisbursementPayment;
import com.academy.fintech.pg.core.disbursement_payment.db.DisbursementPaymentService;
import com.academy.fintech.pg.core.disbursement_payment.db.DisbursementPaymentStatus;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class DisbursementPaymentController
        extends RequestedDisbursementPaymentServiceGrpc.RequestedDisbursementPaymentServiceImplBase {
    private final DisbursementPaymentMerchantService disbursementPaymentMerchantService;
    private final DisbursementPaymentService disbursementPaymentService;

    @Override
    public void requestDisbursement(DisbursementRequest request, StreamObserver<DisbursementRequestResponse> observer) {
        log.info("Got request: {}", request);

        String paymentId = disbursementPaymentMerchantService.requestPayment(request.getEmail(),
                request.getDisbursementAmount());

        DisbursementPayment unprocessedPayment = new DisbursementPayment();
        unprocessedPayment.setId(paymentId);
        unprocessedPayment.setEmail(request.getEmail());
        unprocessedPayment.setDisbursementAmount(request.getDisbursementAmount());
        unprocessedPayment.setStatus(DisbursementPaymentStatus.UNPROCESSED);
        disbursementPaymentService.save(unprocessedPayment);

        observer.onNext(
                DisbursementRequestResponse.newBuilder()
                        .setPaymentId(paymentId).build()
        );
        observer.onCompleted();
    }
}
