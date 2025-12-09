package com.coopcredit.creditapplicationservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "affiliates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Affiliate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String document;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal salary;

    @Column(name = "affiliation_date", nullable = false)
    private LocalDate affiliationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AffiliateStatus status;
}
