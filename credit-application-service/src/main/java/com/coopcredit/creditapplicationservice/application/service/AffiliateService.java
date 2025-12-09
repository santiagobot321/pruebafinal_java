package com.coopcredit.creditapplicationservice.application.service;

import com.coopcredit.creditapplicationservice.application.port.in.AffiliateServicePort;
import com.coopcredit.creditapplicationservice.application.port.out.AffiliatePersistencePort;
import com.coopcredit.creditapplicationservice.domain.model.Affiliate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AffiliateService implements AffiliateServicePort {

    private final AffiliatePersistencePort affiliatePersistencePort;

    @Override
    public Affiliate registerAffiliate(Affiliate affiliate) {
        // TODO: Add business validations (e.g., document unique)
        return affiliatePersistencePort.saveAffiliate(affiliate);
    }

    @Override
    public Affiliate updateAffiliate(Long id, Affiliate affiliateDetails) {
        return affiliatePersistencePort.findAffiliateById(id)
                .map(existingAffiliate -> {
                    existingAffiliate.setName(affiliateDetails.getName());
                    existingAffiliate.setSalary(affiliateDetails.getSalary());
                    existingAffiliate.setStatus(affiliateDetails.getStatus());
                    // Document and affiliation date should not be updated
                    return affiliatePersistencePort.saveAffiliate(existingAffiliate);
                })
                .orElseThrow(() -> new RuntimeException("Affiliate not found with id: " + id)); // TODO: Custom exception
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Affiliate> getAffiliateById(Long id) {
        return affiliatePersistencePort.findAffiliateById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Affiliate> getAffiliateByDocument(String document) {
        return affiliatePersistencePort.findAffiliateByDocument(document);
    }
}
