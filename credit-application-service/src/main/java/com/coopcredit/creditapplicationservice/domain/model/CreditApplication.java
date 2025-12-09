package com.coopcredit.creditapplicationservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "credit_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_id", nullable = false)
    private Affiliate affiliate;

    @Column(name = "requested_amount", nullable = false)
    private BigDecimal requestedAmount;

    @Column(nullable = false)
    private Integer termMonths;

    @Column(name = "proposed_rate", nullable = false)
    private BigDecimal proposedRate;

    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreditApplicationStatus status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "risk_evaluation_id", referencedColumnName = "id")
    private RiskEvaluation riskEvaluation;
}
