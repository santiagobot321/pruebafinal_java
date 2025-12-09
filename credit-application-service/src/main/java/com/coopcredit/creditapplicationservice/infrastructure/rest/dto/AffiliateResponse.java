package com.coopcredit.creditapplicationservice.infrastructure.rest.dto;

import com.coopcredit.creditapplicationservice.domain.model.AffiliateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateResponse {
    private Long id;
    private String document;
    private String name;
    private BigDecimal salary;
    private LocalDate affiliationDate;
    private AffiliateStatus status;
}
