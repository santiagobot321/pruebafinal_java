package com.coopcredit.creditapplicationservice.infrastructure.persistence.jpa.adapter;

import com.coopcredit.creditapplicationservice.application.port.out.AffiliatePersistencePort;
import com.coopcredit.creditapplicationservice.domain.model.Affiliate;
import com.coopcredit.creditapplicationservice.infrastructure.persistence.jpa.repository.AffiliateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaAffiliateAdapter implements AffiliatePersistencePort {

    private final AffiliateRepository affiliateRepository;

    @Override
    public Affiliate saveAffiliate(Affiliate affiliate) {
        return affiliateRepository.save(affiliate);
    }

    @Override
    public Optional<Affiliate> findAffiliateById(Long id) {
        return affiliateRepository.findById(id);
    }

    @Override
    public Optional<Affiliate> findAffiliateByDocument(String document) {
        return affiliateRepository.findByDocument(document);
    }
}
