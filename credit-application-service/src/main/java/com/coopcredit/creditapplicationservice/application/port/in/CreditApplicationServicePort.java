package com.coopcredit.creditapplicationservice.application.port.in;

import com.coopcredit.creditapplicationservice.domain.model.CreditApplication;

import java.util.List;
import java.util.Optional;

public interface CreditApplicationServicePort {
    CreditApplication createApplication(CreditApplication creditApplication);
    CreditApplication evaluateApplication(Long applicationId);
    Optional<CreditApplication> getApplicationById(Long id);
    List<CreditApplication> getApplicationsByAffiliateId(Long affiliateId);
}
