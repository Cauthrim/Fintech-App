package com.academy.fintech.origination.core.pg.client.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "origination.client.pg.grpc")
public record PaymentGrpcClientProperty(String host, int port) {}
