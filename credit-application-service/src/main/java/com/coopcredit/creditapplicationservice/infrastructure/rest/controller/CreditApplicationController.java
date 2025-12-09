package com.coopcredit.creditapplicationservice.infrastructure.rest.controller;

import com.coopcredit.creditapplicationservice.application.port.in.CreditApplicationServicePort;
import com.coopcredit.creditapplicationservice.domain.model.CreditApplication;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.CreditApplicationRequest;
import com.coopcredit.creditapplicationservice.infrastructure.rest.dto.CreditApplicationResponse;
import com.coopcredit.creditapplicationservice.infrastructure.rest.mapper.CreditApplicationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/credit-applications")
@RequiredArgsConstructor
public class CreditApplicationController {

    private final CreditApplicationServicePort creditApplicationServicePort;
    private final CreditApplicationMapper creditApplicationMapper;

    @PostMapping
    public ResponseEntity<CreditApplicationResponse> createCreditApplication(@Valid @RequestBody CreditApplicationRequest request) {
        CreditApplication creditApplication = creditApplicationMapper.toDomain(request);
        CreditApplication createdApplication = creditApplicationServicePort.createApplication(creditApplication);
        return new ResponseEntity<>(creditApplicationMapper.toResponse(createdApplication), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/evaluate")
    public ResponseEntity<CreditApplicationResponse> evaluateCreditApplication(@PathVariable Long id) {
        CreditApplication evaluatedApplication = creditApplicationServicePort.evaluateApplication(id);
        return ResponseEntity.ok(creditApplicationMapper.toResponse(evaluatedApplication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditApplicationResponse> getCreditApplicationById(@PathVariable Long id) {
        return creditApplicationServicePort.getApplicationById(id)
                .map(creditApplicationMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/affiliate/{affiliateId}")
    public ResponseEntity<List<CreditApplicationResponse>> getCreditApplicationsByAffiliateId(@PathVariable Long affiliateId) {
        List<CreditApplication> applications = creditApplicationServicePort.getApplicationsByAffiliateId(affiliateId);
        List<CreditApplicationResponse> responses = applications.stream()
                .map(creditApplicationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
