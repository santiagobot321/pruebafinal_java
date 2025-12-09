package com.coopcredit.creditapplicationservice.infrastructure.rest.mapper;

import com.coopcredit.creditapplicationservice.domain.model.RiskEvaluation;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.RiskEvaluationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RiskEvaluationMapper {
    RiskEvaluationResponse toResponse(RiskEvaluation riskEvaluation);
}
