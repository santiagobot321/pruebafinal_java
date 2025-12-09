package com.coopcredit.creditapplicationservice.application.port.out;

import com.coopcredit.creditapplicationservice.domain.model.CreditApplication;

import java.util.List;
import java.util.Optional;

public interface CreditApplicationPersistencePort {
    CreditApplication saveCreditApplication(CreditApplication creditApplication);
    Optional<CreditApplication> findCreditApplicationById(Long id);
    List<CreditApplication> findCreditApplicationsByAffiliateId(Long affiliateId);
}
