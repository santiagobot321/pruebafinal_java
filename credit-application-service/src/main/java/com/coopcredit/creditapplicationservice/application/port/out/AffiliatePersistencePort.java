package com.coopcredit.creditapplicationservice.application.port.out;

import com.coopcredit.creditapplicationservice.domain.model.Affiliate;

import java.util.Optional;

public interface AffiliatePersistencePort {
    Affiliate saveAffiliate(Affiliate affiliate);
    Optional<Affiliate> findAffiliateById(Long id);
    Optional<Affiliate> findAffiliateByDocument(String document);
}
