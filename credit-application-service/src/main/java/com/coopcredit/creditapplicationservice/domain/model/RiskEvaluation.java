package com.coopcredit.creditapplicationservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "risk_evaluations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String document;

    @Column(nullable = false)
    private Integer score;

    @Column(name = "risk_level", nullable = false)
    private String riskLevel; // ALTO, MEDIO, BAJO

    @Column(nullable = false)
    private String detail;
}
