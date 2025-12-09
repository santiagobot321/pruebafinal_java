package com.coopcredit.creditapplicationservice.application.service;

import com.coopcredit.creditapplicationservice.application.port.in.CreditApplicationServicePort;
import com.coopcredit.creditapplicationservice.application.port.out.AffiliatePersistencePort;
import com.coopcredit.creditapplicationservice.application.port.out.CreditApplicationPersistencePort;
import com.coopcredit.creditapplicationservice.application.port.out.RiskCentralPort;
import com.coopcredit.creditapplicationservice.application.port.out.riskcentral.RiskEvaluationRequest;
import com.coopcredit.creditapplicationservice.application.port.out.riskcentral.RiskEvaluationResponse;
import com.coopcredit.creditapplicationservice.domain.exception.AffiliateNotFoundException;
import com.coopcredit.creditapplicationservice.domain.exception.BusinessValidationException;
import com.coopcredit.creditapplicationservice.domain.exception.CreditApplicationNotFoundException;
import com.coopcredit.creditapplicationservice.domain.model.Affiliate;
import com.coopcredit.creditapplicationservice.domain.model.AffiliateStatus;
import com.coopcredit.creditapplicationservice.domain.model.CreditApplication;
import com.coopcredit.creditapplicationservice.domain.model.CreditApplicationStatus;
import com.coopcredit.creditapplicationservice.domain.model.RiskEvaluation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditApplicationService implements CreditApplicationServicePort {

    private final CreditApplicationPersistencePort creditApplicationPersistencePort;
    private final AffiliatePersistencePort affiliatePersistencePort;
    private final RiskCentralPort riskCentralPort;

    private static final int MIN_AFFILIATION_MONTHS = 6;
    private static final double MAX_DEBT_TO_INCOME_RATIO = 0.35; // 35%
    private static final int MAX_LOAN_SALARY_MULTIPLIER = 5; // Max loan is 5 times salary

    @Override
    @Transactional
    public CreditApplication createApplication(CreditApplication creditApplication) {
        Affiliate affiliate = affiliatePersistencePort.findAffiliateById(creditApplication.getAffiliate().getId())
                .orElseThrow(() -> new AffiliateNotFoundException("Affiliate not found with ID: " + creditApplication.getAffiliate().getId()));

        if (affiliate.getStatus() != AffiliateStatus.ACTIVO) {
            throw new BusinessValidationException("Affiliate is not active and cannot apply for a credit.");
        }
        if (creditApplication.getRequestedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessValidationException("Requested amount must be greater than zero.");
        }
        if (creditApplication.getTermMonths() <= 0) {
            throw new BusinessValidationException("Term in months must be greater than zero.");
        }

        creditApplication.setAffiliate(affiliate); // Ensure managed entity is set
        creditApplication.setStatus(CreditApplicationStatus.PENDIENTE);
        creditApplication.setApplicationDate(LocalDate.now());

        return creditApplicationPersistencePort.saveCreditApplication(creditApplication);
    }

    @Override
    @Transactional
    public CreditApplication evaluateApplication(Long applicationId) {
        CreditApplication application = creditApplicationPersistencePort.findCreditApplicationById(applicationId)
                .orElseThrow(() -> new CreditApplicationNotFoundException("Credit application not found with ID: " + applicationId));

        if (application.getStatus() != CreditApplicationStatus.PENDIENTE) {
            throw new BusinessValidationException("Credit application is not in PENDIENTE status for evaluation.");
        }

        Affiliate affiliate = application.getAffiliate(); // Already fetched with the application if using @EntityGraph or join fetch

        // 1. Invoke Risk Central Service
        RiskEvaluationRequest riskRequest = new RiskEvaluationRequest(
                affiliate.getDocument(),
                application.getRequestedAmount(),
                application.getTermMonths()
        );
        RiskEvaluationResponse riskResponse = riskCentralPort.evaluateRisk(riskRequest);

        // Map RiskEvaluationResponse to RiskEvaluation entity
        RiskEvaluation riskEvaluation = new RiskEvaluation();
        riskEvaluation.setDocument(riskResponse.getDocumento());
        riskEvaluation.setScore(riskResponse.getScore());
        riskEvaluation.setRiskLevel(riskResponse.getNivelRiesgo());
        riskEvaluation.setDetail(riskResponse.getDetalle());

        application.setRiskEvaluation(riskEvaluation); // CascadeType.ALL will save this

        // 2. Apply Internal Policies
        boolean approved = true;
        String rejectionReason = "";

        // Policy 1: Affiliate Status (already checked in create, but good to re-confirm)
        if (affiliate.getStatus() != AffiliateStatus.ACTIVO) {
            approved = false;
            rejectionReason = "Affiliate is not active.";
        }

        // Policy 2: Minimum affiliation antiquity
        if (approved && !isAffiliateOldEnough(affiliate.getAffiliationDate(), MIN_AFFILIATION_MONTHS)) {
            approved = false;
            rejectionReason = "Affiliate does not meet minimum affiliation antiquity.";
        }

        // Policy 3: Debt-to-income ratio (simplified monthly payment calculation)
        // This is a very simplified calculation. A real one would use amortization formulas.
        BigDecimal monthlyPayment = calculateSimplifiedMonthlyPayment(
                application.getRequestedAmount(),
                application.getTermMonths(),
                application.getProposedRate()
        );

        BigDecimal maxAllowedMonthlyPayment = affiliate.getSalary().multiply(BigDecimal.valueOf(MAX_DEBT_TO_INCOME_RATIO));

        if (approved && monthlyPayment.compareTo(maxAllowedMonthlyPayment) > 0) {
            approved = false;
            rejectionReason = "Monthly payment exceeds allowed debt-to-income ratio.";
        }

        // Policy 4: Maximum loan amount based on salary
        BigDecimal maxLoanAmount = affiliate.getSalary().multiply(BigDecimal.valueOf(MAX_LOAN_SALARY_MULTIPLIER));
        if (approved && application.getRequestedAmount().compareTo(maxLoanAmount) > 0) {
            approved = false;
            rejectionReason = "Requested amount exceeds maximum allowed based on salary.";
        }

        // Policy 5: Risk Score (example: reject if HIGH risk)
        if (approved && "ALTO".equalsIgnoreCase(riskEvaluation.getRiskLevel())) {
            approved = false;
            rejectionReason = "High risk level detected by Risk Central.";
        }

        // 3. Update Application Status
        if (approved) {
            application.setStatus(CreditApplicationStatus.APROBADO);
        } else {
            application.setStatus(CreditApplicationStatus.RECHAZADO);
            // Optionally, store rejection reason in RiskEvaluation or a new field in CreditApplication
            riskEvaluation.setDetail(riskEvaluation.getDetail() + " - Motivo de rechazo interno: " + rejectionReason);
        }

        return creditApplicationPersistencePort.saveCreditApplication(application);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CreditApplication> getApplicationById(Long id) {
        return creditApplicationPersistencePort.findCreditApplicationById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditApplication> getApplicationsByAffiliateId(Long affiliateId) {
        return creditApplicationPersistencePort.findCreditApplicationsByAffiliateId(affiliateId);
    }

    private boolean isAffiliateOldEnough(LocalDate affiliationDate, int minMonths) {
        return Period.between(affiliationDate, LocalDate.now()).toTotalMonths() >= minMonths;
    }

    // Simplified monthly payment calculation (e.g., principal / term + interest)
    // This is a very basic approximation. A real-world scenario would use a more accurate amortization formula.
    private BigDecimal calculateSimplifiedMonthlyPayment(BigDecimal principal, Integer termMonths, BigDecimal annualRate) {
        if (termMonths == 0 || principal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP); // annual rate % to monthly decimal
        BigDecimal interest = principal.multiply(monthlyRate).multiply(BigDecimal.valueOf(termMonths));
        return (principal.add(interest)).divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);
    }
}
