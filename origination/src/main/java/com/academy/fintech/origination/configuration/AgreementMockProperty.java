package com.academy.fintech.origination.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "agreement.mock")
public record AgreementMockProperty(String productCode, String interest, String originationAmount, int term) {}
