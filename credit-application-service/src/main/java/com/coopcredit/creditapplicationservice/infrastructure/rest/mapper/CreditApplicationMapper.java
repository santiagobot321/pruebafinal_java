package com.coopcredit.creditapplicationservice.infrastructure.rest.mapper;

import com.coopcredit.creditapplicationservice.domain.model.Affiliate;
import com.coopcredit.creditapplicationservice.domain.model.CreditApplication;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.CreditApplicationRequest;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.CreditApplicationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AffiliateMapper.class, RiskEvaluationMapper.class})
public interface CreditApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicationDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "riskEvaluation", ignore = true)
    @Mapping(source = "affiliateId", target = "affiliate")
    CreditApplication toDomain(CreditApplicationRequest request);

    default Affiliate mapAffiliateIdToAffiliate(Long affiliateId) {
        if (affiliateId == null) {
            return null;
        }
        Affiliate affiliate = new Affiliate();
        affiliate.setId(affiliateId);
        return affiliate;
    }

    CreditApplicationResponse toResponse(CreditApplication creditApplication);
}
