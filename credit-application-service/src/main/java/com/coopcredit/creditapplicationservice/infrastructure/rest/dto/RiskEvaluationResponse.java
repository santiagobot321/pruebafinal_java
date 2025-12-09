package com.coopcredit.creditapplicationservice.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationResponse {
    private Long id;
    private String document;
    private Integer score;
    private String riskLevel;
    private String detail;
}
