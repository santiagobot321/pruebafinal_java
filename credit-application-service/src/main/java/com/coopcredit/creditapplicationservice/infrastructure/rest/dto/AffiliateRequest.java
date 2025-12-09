package com.coopcredit.creditapplicationservice.infrastructure.rest.dto;

import com.coopcredit.creditapplicationservice.domain.model.AffiliateStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateRequest {
    @NotBlank(message = "Document cannot be empty")
    private String document;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Salary cannot be null")
    @DecimalMin(value = "0.01", message = "Salary must be greater than 0")
    private BigDecimal salary;

    @NotNull(message = "Affiliation date cannot be null")
    @PastOrPresent(message = "Affiliation date cannot be in the future")
    private LocalDate affiliationDate;

    @NotNull(message = "Status cannot be null")
    private AffiliateStatus status;
}
