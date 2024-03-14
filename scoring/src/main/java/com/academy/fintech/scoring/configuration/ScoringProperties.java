package com.academy.fintech.scoring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "scoring")
public record ScoringProperties(int significantOverduePeriod, int salaryThreshold) {}
