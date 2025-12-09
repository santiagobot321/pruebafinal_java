package com.coopcredit.creditapplicationservice.domain.exception;

public class CreditApplicationNotFoundException extends RuntimeException {
    public CreditApplicationNotFoundException(String message) {
        super(message);
    }
}
