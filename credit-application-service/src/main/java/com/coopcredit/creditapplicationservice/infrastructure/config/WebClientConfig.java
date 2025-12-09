package com.coopcredit.creditapplicationservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${risk-central.service.url}")
    private String riskCentralServiceUrl;

    @Bean
    public WebClient riskCentralWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(riskCentralServiceUrl).build();
    }
}
