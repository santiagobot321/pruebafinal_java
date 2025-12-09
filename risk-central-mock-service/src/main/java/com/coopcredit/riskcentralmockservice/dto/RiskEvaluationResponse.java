package com.coopcredit.riskcentralmockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationResponse {
    private String documento;
    private Integer score;
    private String nivelRiesgo;
    private String detalle;
}
