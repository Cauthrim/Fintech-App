package com.academy.fintech.origination.core.pe.client.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "origination.client.pe.grpc")
public record AgreementGrpcClientProperty(String host, int port) { }
