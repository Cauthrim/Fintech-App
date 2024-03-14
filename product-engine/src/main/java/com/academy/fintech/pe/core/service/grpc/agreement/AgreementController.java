package com.academy.fintech.pe.core.service.grpc.agreement;

import com.academy.fintech.agreement.*;
import com.academy.fintech.pe.core.service.agreement.AgreementActivationService;
import com.academy.fintech.pe.core.service.agreement.AgreementCreationService;
import com.academy.fintech.pe.core.service.agreement.db.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.AgreementStatus;
import com.academy.fintech.pe.core.service.schedule.db.schedule.PaymentSchedule;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@GRpcService
@Slf4j
@RequiredArgsConstructor
public class AgreementController extends AgreementServiceGrpc.AgreementServiceImplBase {
    private final AgreementActivationService agreementActivationService;
    private final AgreementCreationService agreementCreationService;

    @Override
    public void createAgreement(ProtoAgreement protoAgreement, StreamObserver<ProtoAgreementId> observer) {
        log.info("Got request: {}", protoAgreement);

        Agreement agreement = getAgreementFromProto(protoAgreement);

        ProtoAgreementId response = ProtoAgreementId.newBuilder()
                .setId(agreementCreationService.createAgreement(agreement))
                .build();
        observer.onNext(response);
        observer.onCompleted();
    }

    @Override
    @Transactional
    public void activateAgreement(AgreementActivation activation, StreamObserver<ProtoPaymentSchedule> observer) {
        log.info("Got request: {}", activation);

        PaymentSchedule paymentSchedule = agreementActivationService.activateAgreement(activation.getAgreementId(),
                LocalDate.parse(activation.getDate()));

        ProtoPaymentSchedule response = ProtoPaymentSchedule.newBuilder()
                .setId(paymentSchedule.getId())
                .setAgreementId(paymentSchedule.getAgreementId())
                .setVersion(paymentSchedule.getVersion())
                .build();
        observer.onNext(response);
        observer.onCompleted();
    }

    private Agreement getAgreementFromProto(ProtoAgreement protoAgreement) {
        Agreement agreement = new Agreement();
        agreement.setProductCode(protoAgreement.getProductCode());
        agreement.setClientId(protoAgreement.getClientId());
        agreement.setInterest(new BigDecimal(protoAgreement.getInterest()));
        agreement.setTerm(protoAgreement.getTerm());
        agreement.setDisbursementAmount(new BigDecimal(protoAgreement.getDisbursementAmount()));
        agreement.setOriginationAmount(new BigDecimal(protoAgreement.getOriginationAmount()));
        agreement.setStatus(AgreementStatus.NEW);
        return agreement;
    }
}
