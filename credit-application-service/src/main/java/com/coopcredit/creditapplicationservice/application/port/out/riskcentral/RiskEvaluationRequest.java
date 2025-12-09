package com.coopcredit.creditapplicationservice.application.port.out.riskcentral;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationRequest {
    private String documento;
    private BigDecimal monto;
    private Integer plazo;
}
