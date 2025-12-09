package com.coopcredit.creditapplicationservice.infrastructure.rest.controller;

import com.coopcredit.creditapplicationservice.application.port.in.AffiliateServicePort;
import com.coopcredit.creditapplicationservice.domain.model.Affiliate;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.AffiliateRequest;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.AffiliateResponse;
import com.coopcredit.creditapplicationservice.infrastructure.rest.mapper.AffiliateMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/affiliates")
@RequiredArgsConstructor
public class AffiliateController {

    private final AffiliateServicePort affiliateServicePort;
    private final AffiliateMapper affiliateMapper;

    @PostMapping
    public ResponseEntity<AffiliateResponse> registerAffiliate(@Valid @RequestBody AffiliateRequest request) {
        Affiliate affiliate = affiliateMapper.toDomain(request);
        Affiliate registeredAffiliate = affiliateServicePort.registerAffiliate(affiliate);
        return new ResponseEntity<>(affiliateMapper.toResponse(registeredAffiliate), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AffiliateResponse> updateAffiliate(@PathVariable Long id, @Valid @RequestBody AffiliateRequest request) {
        Affiliate affiliateDetails = affiliateMapper.toDomain(request);
        Affiliate updatedAffiliate = affiliateServicePort.updateAffiliate(id, affiliateDetails);
        return ResponseEntity.ok(affiliateMapper.toResponse(updatedAffiliate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AffiliateResponse> getAffiliateById(@PathVariable Long id) {
        return affiliateServicePort.getAffiliateById(id)
                .map(affiliateMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
