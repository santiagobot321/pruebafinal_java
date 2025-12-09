package com.coopcredit.creditapplicationservice.application.port.out;

import com.coopcredit.creditapplicationservice.application.port.out.riskcentral.RiskEvaluationRequest;
import com.coopcredit.creditapplicationservice.application.port.out.riskcentral.RiskEvaluationResponse;

public interface RiskCentralPort {
    RiskEvaluationResponse evaluateRisk(RiskEvaluationRequest request);
}
