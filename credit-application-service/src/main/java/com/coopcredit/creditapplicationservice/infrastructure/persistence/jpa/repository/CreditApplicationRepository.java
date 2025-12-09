package com.coopcredit.creditapplicationservice.infrastructure.persistence.jpa.repository;

import com.coopcredit.creditapplicationservice.domain.model.CreditApplication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreditApplicationRepository extends JpaRepository<CreditApplication, Long> {

    @EntityGraph(attributePaths = {"affiliate", "riskEvaluation"})
    Optional<CreditApplication> findById(Long id);

    @EntityGraph(attributePaths = {"affiliate", "riskEvaluation"})
    List<CreditApplication> findByAffiliateId(Long affiliateId);
}
