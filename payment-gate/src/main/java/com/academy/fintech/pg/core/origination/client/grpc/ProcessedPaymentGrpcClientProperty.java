package com.academy.fintech.pg.core.origination.client.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pg.client.origination.grpc")
public record ProcessedPaymentGrpcClientProperty(String host, int port) {}
