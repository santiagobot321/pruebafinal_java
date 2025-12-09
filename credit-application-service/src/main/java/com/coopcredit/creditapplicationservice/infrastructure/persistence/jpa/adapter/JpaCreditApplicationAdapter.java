package com.coopcredit.creditapplicationservice.infrastructure.persistence.jpa.adapter;

import com.coopcredit.creditapplicationservice.application.port.out.CreditApplicationPersistencePort;
import com.coopcredit.creditapplicationservice.domain.model.CreditApplication;
import com.coopcredit.creditapplicationservice.infrastructure.persistence.jpa.repository.CreditApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaCreditApplicationAdapter implements CreditApplicationPersistencePort {

    private final CreditApplicationRepository creditApplicationRepository;

    @Override
    public CreditApplication saveCreditApplication(CreditApplication creditApplication) {
        return creditApplicationRepository.save(creditApplication);
    }

    @Override
    public Optional<CreditApplication> findCreditApplicationById(Long id) {
        return creditApplicationRepository.findById(id);
    }

    @Override
    public List<CreditApplication> findCreditApplicationsByAffiliateId(Long affiliateId) {
        return creditApplicationRepository.findByAffiliateId(affiliateId);
    }
}
