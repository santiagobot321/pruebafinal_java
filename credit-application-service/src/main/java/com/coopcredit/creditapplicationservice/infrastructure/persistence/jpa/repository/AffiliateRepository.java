package com.coopcredit.creditapplicationservice.infrastructure.persistence.jpa.repository;

import com.coopcredit.creditapplicationservice.domain.model.Affiliate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AffiliateRepository extends JpaRepository<Affiliate, Long> {
    Optional<Affiliate> findByDocument(String document);
}
