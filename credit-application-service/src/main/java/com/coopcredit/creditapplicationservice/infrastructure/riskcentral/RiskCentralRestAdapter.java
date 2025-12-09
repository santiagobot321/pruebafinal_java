package com.coopcredit.creditapplicationservice.infrastructure.riskcentral;

import com.coopcredit.creditapplicationservice.application.port.out.RiskCentralPort;
import com.coopcredit.creditapplicationservice.application.port.out.riskcentral.RiskEvaluationRequest;
import com.coopcredit.creditapplicationservice.application.port.out.riskcentral.RiskEvaluationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class RiskCentralRestAdapter implements RiskCentralPort {

    @Qualifier("riskCentralWebClient")
    private final WebClient webClient;

    @Override
    public RiskEvaluationResponse evaluateRisk(RiskEvaluationRequest request) {
        return webClient.post()
                .uri("/risk-evaluation")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(RiskEvaluationResponse.class)
                .block(); // Blocking for simplicity in this example, consider reactive approach for production
    }
}
