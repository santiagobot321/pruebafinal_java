package com.coopcredit.creditapplicationservice.infrastructure.rest.mapper;

import com.coopcredit.creditapplicationservice.domain.model.Affiliate;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.AffiliateRequest;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.AffiliateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AffiliateMapper {

    Affiliate toDomain(AffiliateRequest request);

    AffiliateResponse toResponse(Affiliate affiliate);

    @Mapping(target = "id", ignore = true) // ID should not be updated from request
    @Mapping(target = "document", ignore = true) // Document should not be updated
    @Mapping(target = "affiliationDate", ignore = true) // Affiliation date should not be updated
    void updateAffiliateFromRequest(AffiliateRequest request, @MappingTarget Affiliate affiliate);
}
