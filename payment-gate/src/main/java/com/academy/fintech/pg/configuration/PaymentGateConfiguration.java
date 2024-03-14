package com.academy.fintech.pg.configuration;

import com.academy.fintech.pg.core.origination.client.grpc.ProcessedPaymentGrpcClientProperty;
import com.academy.fintech.pg.core.pe.client.grpc.DebitPaymentGrpcClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ DebitPaymentGrpcClientProperty.class,
        MerchantUrlProperty.class,
        ProcessedPaymentGrpcClientProperty.class })
public class PaymentGateConfiguration {
}
