package com.coopcredit.creditapplicationservice.infrastructure.rest.dto;

import com.coopcredit.creditapplicationservice.domain.model.CreditApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditApplicationResponse {
    private Long id;
    private AffiliateResponse affiliate;
    private BigDecimal requestedAmount;
    private Integer termMonths;
    private BigDecimal proposedRate;
    private LocalDate applicationDate;
    private CreditApplicationStatus status;
    private RiskEvaluationResponse riskEvaluation;
}
