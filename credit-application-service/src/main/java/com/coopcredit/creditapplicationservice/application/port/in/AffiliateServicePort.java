package com.coopcredit.creditapplicationservice.application.port.in;

import com.coopcredit.creditapplicationservice.domain.model.Affiliate;

import java.util.Optional;

public interface AffiliateServicePort {
    Affiliate registerAffiliate(Affiliate affiliate);
    Affiliate updateAffiliate(Long id, Affiliate affiliateDetails);
    Optional<Affiliate> getAffiliateById(Long id);
    Optional<Affiliate> getAffiliateByDocument(String document);
}
