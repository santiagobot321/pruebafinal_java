package com.coopcredit.creditapplicationservice.infrastructure.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditApplicationRequest {
    @NotNull(message = "Affiliate ID cannot be null")
    private Long affiliateId;

    @NotNull(message = "Requested amount cannot be null")
    @DecimalMin(value = "0.01", message = "Requested amount must be greater than 0")
    private BigDecimal requestedAmount;

    @NotNull(message = "Term in months cannot be null")
    @Min(value = 1, message = "Term in months must be at least 1")
    private Integer termMonths;

    @NotNull(message = "Proposed rate cannot be null")
    @DecimalMin(value = "0.01", message = "Proposed rate must be greater than 0")
    private BigDecimal proposedRate;
}
