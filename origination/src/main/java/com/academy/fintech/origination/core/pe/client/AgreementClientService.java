package com.academy.fintech.origination.core.pe.client;

import com.academy.fintech.agreement.AgreementActivation;
import com.academy.fintech.agreement.ProtoAgreement;
import com.academy.fintech.origination.core.pe.client.grpc.AgreementGrpcClient;
import com.academy.fintech.origination.public_interface.loan.dto.AgreementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AgreementClientService {
    private final AgreementGrpcClient agreementGrpcClient;

    public String createAgreement(AgreementDto agreementDto) {
        ProtoAgreement protoAgreement = ProtoAgreement.newBuilder()
                .setProductCode(agreementDto.productCode())
                .setClientId(agreementDto.clientId())
                .setInterest(agreementDto.interest())
                .setTerm(agreementDto.term())
                .setDisbursementAmount(agreementDto.disbursementAmount())
                .setOriginationAmount(agreementDto.originationAmount()).build();

        return agreementGrpcClient.createAgreement(protoAgreement).getId();
    }

    public void activateAgreement(String agreementId) {
        AgreementActivation agreementActivation = AgreementActivation.newBuilder()
                .setAgreementId(agreementId)
                .setDate(LocalDate.now().toString()).build();

        agreementGrpcClient.activateAgreement(agreementActivation);
    }
}
