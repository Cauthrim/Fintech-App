package com.academy.fintech.pg.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "merchant.disbursement")
public record MerchantUrlProperty(String requestUrl, String statusUrl) {}
